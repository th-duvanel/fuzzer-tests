import numpy as np
import scipy.stats
from tabulate import tabulate

# Função para ler os dados de um conjunto (CPUs ou memórias RAM)
def ler_dados():
    entrada = input()
    dados = [float(num) for num in entrada.split()]
    return dados

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

# Teste t-Student
t_student_tmp, _ = scipy.stats.ttest_ind(tempo, np.zeros(len(tempo)))
t_student_cpus, _ = scipy.stats.ttest_ind(cpus, np.zeros(len(cpus)))
t_student_rams, _ = scipy.stats.ttest_ind(rams, np.zeros(len(rams)))

# Formatando os valores com duas casas decimais
std_tmp = round(std_tmp, 2)
std_cpus = round(std_cpus, 2)
std_rams = round(std_rams, 2)

conf_interval_tmp = [round(value, 2) for value in conf_interval_tmp]
conf_interval_cpus = [round(value, 2) for value in conf_interval_cpus]
conf_interval_rams = [round(value, 2) for value in conf_interval_rams]

t_student_tmp = round(t_student_tmp, 2)
t_student_cpus = round(t_student_cpus, 2)
t_student_rams = round(t_student_rams, 2)

# Tabela
dados = [
    ["", "Média","Desvio Padrão", "Intervalo de Confiança"],
    ["Tempo (s)",mean_tmp, std_tmp, conf_interval_tmp],
    ["CPUs (%)",mean_cpus, std_cpus, conf_interval_cpus],
    ["Memórias RAM (MiB)",mean_rams, std_rams, conf_interval_rams]
]

tabela = tabulate(dados, headers="firstrow", tablefmt="fancy_grid")
print(tabela)

