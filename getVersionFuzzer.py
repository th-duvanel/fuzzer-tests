from fuzzers_table.tlsfuzzer.tlsfuzzer.tlsfuzzer.messages import Connect
from fuzzers_table.tlsfuzzer.tlsfuzzer.tlsfuzzer.expect import Expect
from fuzzers_table.tlsfuzzer.tlsfuzzer.tlsfuzzer.fuzzers import StructuredRandom
from fuzzers_table.tlsfuzzer.tlsfuzzer.tlsfuzzer.messages import RawSocketWriteGenerator
from fuzzers_table.tlsfuzzer.tlsfuzzer.tlsfuzzer.runner import Runner
import sys

"""
This class represents an error response of the server after
an invalid GET_VERSION message from the client.
"""
class ExpectSPDMVersionErrorResponse(Expect):
    def __init__(self):
        super().__init__(0)
        self.errorMessage = bytearray([0x10, 0x7f])

    def is_match(self, msg):
        response = msg.data
        if len(response) < 4:
            return False
        if self.errorMessage[0] != response[0] or self.errorMessage[1] != response[1]:
            return False

        return True

    def process(self, state, msg):
        return

"""
This class represents the VERSION message of the server.
"""
class ExpectSPDMVersionResponse(Expect):
    def __init__(self):
        super().__init__(0)
        self.errorMessage = bytearray([0x10, 0x04])

    def is_match(self, msg):
        print("Checking match...")
        response = msg.data
        if len(response) != 2*int.from_bytes(response[5]) + 5:
            return False
        if self.errorMessage[0] != response[0] or self.errorMessage[1] != response[1]:
            return False

        return True

    def process(self, state, msg):
        return

class ExpectSPDMEmuResponse(Expect):

    def __init__(self, command, size,  buffer):
        self.command = command
        self.size = size.to_bytes(4, 'big')
        self.buffer = buffer
        super().__init__(0)

    def is_match(self, msg):
        msg = msg.data
        (command, transport, size, buffer) = self.parse_message(msg)

        if buffer != self.buffer:
            return False

        return self.check_if_configs_are_valid(command, transport, size)

    def parse_message(self, msg):
        command = bytearray([int.from_bytes(msg[i]) for i in range(4)])
        transport = bytearray([int.from_bytes(msg[i]) for i in range(4, 8)])
        size = bytearray([int.from_bytes(msg[i]) for i in range(8, 12)])
        buffer = bytearray([int.from_bytes(msg[i]) for i in range(12, len(msg))])
        return (command, transport, size, buffer)

    def check_if_configs_are_valid(self, command, transport, size):

        if command != self.command:
            return False
        if transport != b'\x00\x00\x00\x03':
            return False
        if size != self.size:
            return False
        return True

    def process(self, state, msg):
        return

class ExpectSPDMEmuErrorResponse(ExpectSPDMEmuResponse):
    def __init__(self):
        super().__init__(b'\x00\x00\x00\x01',
                         size,  b'')
        self.errorCode = bytearray([0x10, 0x7f])

    def is_match(self, msg):
        (command, transport, size, buffer) = self.parse_message(msg)

        if (buffer[1] != self.errorCode[0] or buffer[2] != self.errorCode[1]):
            return False

        return self.check_if_configs_are_valid(command, transport, size)

def sendToResponder(rootNode, command, bufferSize, message):
    transport_type = b'\x00\x00\x00\x03'
    size = bufferSize.to_bytes(4, 'big')

    node = rootNode.add_child(RawSocketWriteGenerator(command))
    node = rootNode.add_child(RawSocketWriteGenerator(transport_type))
    node = rootNode.add_child(RawSocketWriteGenerator(size))
    node = rootNode.add_child(RawSocketWriteGenerator(message))

    return node

def sendInitialMessage(root_node):
    return sendToResponder(root_node,
                    b'\x00\x00\xde\xad',
                    14,
                    b'\x43\x6c\x69\x65\x6e\x74\x20\x48\x65\x6c\x6c\x6f\x21\x00')

def sendGetVersion(rootNode, fuzzedMessage):
    return sendToResponder(rootNode,
                           b'\x00\x00\x00\x01',
                           5,
                           fuzzedMessage)

#Platform port Receive command: 00 00 de ad
#Platform port Receive transport_type: 00 00 00 01
#Platform port Receive size: 00 00 00 0e
#Platform port Receive buffer:
#    43 6c 69 65 6e 74 20 48 65 6c 6c 6f 21 00
#Platform port Transmit command: 00 00 de ad
#Platform port Transmit transport_type: 00 00 00 01
#Platform port Transmit size: 00 00 00 0e
#Platform port Transmit buffer:
#    53 65 72 76 65 72 20 48 65 6c 6c 6f 21 00
#Platform port Receive command: 00 00 00 01
#Platform port Receive transport_type: 00 00 00 01
#Platform port Receive size: 00 00 00 05
#Platform port Receive buffer:
#    05 10 84 00 00

# Check if user has passed port number
if (len(sys.argv) != 2):
    print("Error: to run this test specify the port number")
    exit()

# Setting the messages that will be sent
rightVersionWrongCode = StructuredRandom(vals=[(1, 0x10), (3, None)]).data
rightMessage = StructuredRandom(vals=[(1, 0x10), (1, 0x84), (2, 0x0)]).data
wrongVersionRightCode =  StructuredRandom(vals=[(1, None), (1, 0x84), (2, None)]).data
wrongMessage = StructuredRandom(vals=[(4, None)]).data
wrongMessageOverflow = StructuredRandom(vals=[(1000, None)]).data

root_node = Connect("localhost", int(sys.argv[1]))
node = root_node
node = sendInitialMessage(node)
node = node.add_child(ExpectSPDMEmuResponse(b'\x00\x00\xde\xad',
                                            14,
                                            b'\x53\x65\x72\x76\x65\x72\x20\x48\x65\x6c\x6c\x6f\x21\x00'))
node = sendGetVersion(node)
node = node.add_child(Expect)

runner = Runner(root_node)
runner.run()
