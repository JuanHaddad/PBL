// Obtém os parâmetros da URL usando URLSearchParams
const id_simulacao = parametrosSimulacao.id_simulacao;
const duracaoSimulacao = parametrosSimulacao.duracao;
const frequencia = parametrosSimulacao.frequencia;
const comprimentoOnda = parametrosSimulacao.comprimentoOnda;
const erroMaximo = parametrosSimulacao.erroMaximo;

console.log("id_simulacao: ", id_simulacao);
console.log("Duração da Simulação:", duracaoSimulacao);
console.log("Frequência:", frequencia);
console.log("Comprimento de Onda:", comprimentoOnda);
console.log("Erro Máximo:", erroMaximo);

// Variáveis de animação
let t = 0;  // Tempo inicial
const passoTempo = 0.027;  // Passo de tempo (em segundos) NÃO MUDAR ISSO
const fps = 60;  // Frames por segundo da animação

// Função para gerar os pontos da onda com base no tempo usando o backend
function calcularPontosOnda(t) {
    return fetch(`/calcularPontosOnda?frequencia=${frequencia}&comprimentoOnda=${comprimentoOnda}&tempo=${t}&erroMaximo=${erroMaximo}`)
        .then(response => response.json())
        .then(data => {
            return {
                dadosX: data.dadosX,
                dadosY: data.dadosY
            };
        });
}

// Função para calcular o valor da onda no ponto X fixo para o tempo t usando o backend
function calcularValorNoPonto(t) {
    return fetch(`/calcularValorNoPonto?frequencia=${frequencia}&comprimentoOnda=${comprimentoOnda}&tempo=${t}&erroMaximo=${erroMaximo}`)
        .then(response => response.json());
}

// Configurando o gráfico com Chart.js
var ctx = document.getElementById('graficoOnda').getContext('2d');

var chart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: [],  // Iniciamos vazio
        datasets: [{
            label: 'Onda Transversal',
            data: [],  // Inicialmente vazio
            borderColor: '#f2b6c0',
            borderWidth: 2,
            fill: false,
            pointRadius: 1,
        },
            {
                label: 'Ponto Fixo na Onda',
                data: [{ x: 1, y: 0 }],
                borderColor: '#e3e3e3',
                borderWidth: 5,
                showLine: false,
                pointRadius: 5,
                pointBackgroundColor: 'red'
            }]
    },
    options: {
        animation: false,
        scales: {
            x: {
                type: 'linear',
                position: 'bottom',
                min: 0,
                max: 2,
                ticks: { color: '#e3e3e3' },
                grid: { color: 'rgba(227, 227, 227, 0.2)' }
            },
            y: {
                beginAtZero: false,
                min: -1,
                max: 1,
                ticks: { color: '#e3e3e3' },
                grid: { color: 'rgba(227, 227, 227, 0.2)' }
            }
        }
    }
});

// Função para atualizar o gráfico com animação para a onda e o ponto fixo
function atualizarGrafico() {
    if (t <= duracaoSimulacao) {
        console.log("Atualizando gráfico, t = ", t);

        calcularPontosOnda(t).then(pontosOnda => {
            chart.data.labels = pontosOnda.dadosX;
            chart.data.datasets[0].data = pontosOnda.dadosY;

            // Calcula o valor de y no ponto fixo em x = 1
            calcularValorNoPonto(t).then(valorNoPonto => {
                chart.data.datasets[1].data = [{ x: 1, y: valorNoPonto }];
                chart.update();

                t += passoTempo;  // Avançamos o tempo
                setTimeout(atualizarGrafico, 1000 / fps);  // Próximo frame da animação
            });
        });
    } else {
        console.log("Animação finalizada no TEMPO(t) = ", t);
    }
}

// Iniciar a animação do gráfico
atualizarGrafico();


function mostrarPontoNoTempo() {
    const tempo = parseFloat(document.getElementById('tempo').value);

    if (!id_simulacao || isNaN(tempo) || tempo < 0 || tempo > 10) {
        alert("Por favor, insira um valor válido para o tempo entre 0 e 10.");
        return;
    }

    // Certifique-se de incluir todos os parâmetros necessários na URL
    const url = `/pontoNoTempo?id_simulacao=${id_simulacao}&frequencia=${frequencia}&comprimentoOnda=${comprimentoOnda}&tempo=${tempo}&erroMaximo=${erroMaximo}`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erro na requisição: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const posicaoY = data.posicaoY;
            if (posicaoY !== null) {
                // Atualiza o ponto fixo com o valor de Y no tempo solicitado
                chart.data.datasets[1].data = [{ x: 1, y: posicaoY }];

                // Atualiza a linha da onda para o tempo solicitado
                calcularPontosOnda(tempo).then(pontosOnda => {
                    chart.data.labels = pontosOnda.dadosX;
                    chart.data.datasets[0].data = pontosOnda.dadosY;
                    chart.update();
                });
            } else {
                alert("Ponto não encontrado para o tempo especificado.");
            }
        })
        .catch(error => console.error("Erro ao buscar o ponto:", error));
}
