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
        if len(response) != 2*response[5] + 5:
            return False
        if self.errorMessage[0] != response[0] or self.errorMessage[1] != response[1]:
            return False

        return True

    def process(self, state, msg):
        return

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

#node = node.add_child(RawSocketWriteGenerator(rightVersionWrongCode))
#node = node.add_child(ExpectSPDMVersionErrorResponse())

#node = node.add_child(RawSocketWriteGenerator(wrongMessage))
#node = node.add_child(ExpectSPDMVersionErrorResponse())

#node = node.add_child(RawSocketWriteGenerator(wrongMessageOverflow))
#node = node.add_child(ExpectSPDMVersionErrorResponse())

#node = node.add_child(RawSocketWriteGenerator(wrongVersionRightCode))
#node = node.add_child(ExpectSPDMVersionErrorResponse())
print(rightMessage)
node = node.add_child(RawSocketWriteGenerator(rightMessage))
node = node.add_child(ExpectSPDMVersionResponse())

runner = Runner(root_node)
runner.run()
