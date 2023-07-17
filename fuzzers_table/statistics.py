import numpy as np
import scipy.stats
from tabulate import tabulate


def ler_dados():
    entrada = input()
    dados = [float(num) for num in entrada.split()]
    return dados

# Dicionário de fuzzers e seus nomes
fuzzers = {
    "TLS-ATTACKER": "TLS Attacker",
    "TLSFUZZER": "TLSFuzzer",
    "TLSDIFF": "TLSdiff",

}

# Dicionário para armazenar as tabelas
tabelas_fuzzers = {}

# Iterar sobre cada fuzzer
for fuzzer, nome in fuzzers.items():
    print(f"--- Fuzzer: {nome} ---")

    # Leitura
    tempo = ler_dados()
    cpus = ler_dados()
    rams = ler_dados()

    mean_tmp = np.mean(tempo)
    mean_cpus = np.mean(cpus)
    mean_rams = np.mean(rams)

    # Desvio padrão
    std_tmp = np.std(tempo)
    std_cpus = np.std(cpus)
    std_rams = np.std(rams)

    # Intervalo de confiança (95% de confiança)
    conf_interval_tmp = scipy.stats.t.interval(0.95, len(tempo) - 1, loc=mean_tmp, scale=std_tmp)
    conf_interval_cpus = scipy.stats.t.interval(0.95, len(cpus) - 1, loc=mean_cpus, scale=std_cpus)
    conf_interval_rams = scipy.stats.t.interval(0.95, len(rams) - 1, loc=mean_rams, scale=std_rams)

    # Tabela do fuzzer
    dados = [
        ["", "Valor", "Média", "Desvio Padrão", "Intervalo de Confiança"],
        ["Tempo (s)", "Média", mean_tmp, std_tmp, conf_interval_tmp],
        ["CPUs (%)", "Média", mean_cpus, std_cpus, conf_interval_cpus],
        ["Memórias RAM (MiB)", "Média", mean_rams, std_rams, conf_interval_rams]
    ]

    # Armazenar tabela do fuzzer no dicionário
    tabelas_fuzzers[nome] = dados

# Tabela com todos os fuzzers
tabela_final = [
    ["Fuzzer", "Tipo", "Tempo (s)", "CPUs (%)", "Memórias RAM (MiB)"],
]

for nome, dados in tabelas_fuzzers.items():
    media_tempo = dados[1][2]
    desvio_padrao_tempo = dados[1][3]
    intervalo_confianca_tempo = dados[1][4]

    media_cpus = dados[2][2]
    desvio_padrao_cpus = dados[2][3]
    intervalo_confianca_cpus = dados[2][4]

    media_rams = dados[3][2]
    desvio_padrao_rams = dados[3][3]
    intervalo_confianca_rams = dados[3][4]

    tabela_final.append([nome, "Média", media_tempo, media_cpus, media_rams])
    tabela_final.append(["", "Desvio Padrão", desvio_padrao_tempo, desvio_padrao_cpus, desvio_padrao_rams])
    tabela_final.append(["", "Intervalo de Confiança", intervalo_confianca_tempo, intervalo_confianca_cpus, intervalo_confianca_rams])

# Imprimir tabela final
print("\n--- Tabela de todos os fuzzers ---")
tabela = tabulate(tabela_final, headers="firstrow", tablefmt="fancy_grid")
print(tabela)
