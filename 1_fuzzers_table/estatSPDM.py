import numpy as np
import scipy.stats
import matplotlib.pyplot as plt

# Função para ler os dados de um conjunto (CPUs ou memórias RAM)
def ler_dados():
    entrada = input()
    dados = [float(num) for num in entrada.split()]
    return dados

tempo = ler_dados()
cpus = ler_dados()
rams = ler_dados()

# Desvio padrão
std_tmp = np.std(tempo)
std_cpus = np.std(cpus)
std_rams = np.std(rams)

# Intervalo de confiança (95% de confiança)
conf_interval_tmp = scipy.stats.t.interval(0.95, len(tempo) - 1, loc=np.mean(tempo), scale=std_tmp)
conf_interval_cpus = scipy.stats.t.interval(0.95, len(cpus) - 1, loc=np.mean(cpus), scale=std_cpus)
conf_interval_rams = scipy.stats.t.interval(0.95, len(rams) - 1, loc=np.mean(rams), scale=std_rams)

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
    ["", "Desvio Padrão", "Intervalo de Confiança"],
    ["Tempo", std_tmp, conf_interval_tmp],
    ["CPUs", std_cpus, conf_interval_cpus],
    ["Memórias RAM", std_rams, conf_interval_rams]
]

fig, ax = plt.subplots(figsize=(8, 4)) 
ax.axis('off')
table = ax.table(cellText=dados, colLabels=None, cellLoc='center', loc='center')
table.auto_set_font_size(False)
table.set_fontsize(10)  
table.scale(1.3, 1.5)  

plt.savefig('tabela.png', bbox_inches='tight')
plt.show()
