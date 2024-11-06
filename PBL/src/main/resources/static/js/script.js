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
const passoTempo = 0.0175;  // Passo de tempo (em segundos) NÃO MUDA ISSO DE FORMA ALGUMA
const fps = 60;  // Frames por segundo da animação

// Função para calcular o seno usando série de Taylor com ajuste de argumento
function calcularSenoTaylor(valor, erroMaximo) {
    valor = valor % (2 * Math.PI);
    if (valor > Math.PI) valor -= 2 * Math.PI;
    else if (valor < -Math.PI) valor += 2 * Math.PI;

    let termo = valor;  // Primeiro termo da série (x^1 / 1!)
    let soma = termo;
    let n = 1;

    while (Math.abs(termo) > erroMaximo) {
        termo *= -1 * valor * valor / ((2 * n) * (2 * n + 1));
        soma += termo;
        n++;
    }

    return soma;
}

// Função para gerar os pontos da onda com base no tempo
function calcularPontosOnda(t) {
    let dadosX = [];
    let dadosY = [];
    const passoX = 0.01;  // Reduzimos o passo ainda mais para capturar detalhes finos

    for (let x = 0; x <= 2; x += passoX) {
        let argumento = 2 * Math.PI * (frequencia * t - x / comprimentoOnda);
        let y = calcularSenoTaylor(argumento, erroMaximo);
        dadosX.push(x);
        dadosY.push(y);
    }

    return { dadosX, dadosY };
}

// Função para calcular o valor da onda no ponto X fixo para o tempo t
function calcularValorNoPonto(t) {
    const posicaoX = 1;  // Mantém o ponto vermelho fixo em x = 1
    const argumento = 2 * Math.PI * (frequencia * t - posicaoX / comprimentoOnda);
    return calcularSenoTaylor(argumento, erroMaximo);
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
            pointRadius: 1,  // Diminui o tamanho do ponto para 1
        },
        {
            label: 'Ponto Fixo na Onda',
            data: [{ x: 1, y: 0 }],  // Inicialmente em (x = 1, y = 0)
            borderColor: '#e3e3e3',  // Ponto vermelho
            borderWidth: 5,
            showLine: false,  // Mostra apenas o ponto, sem linha conectada
            pointRadius: 5,
            pointBackgroundColor: 'red'
        }]
    },
    options: {
        animation: false,  // Desabilitamos a animação nativa
        scales: {
            x: {
                type: 'linear',
                position: 'bottom',
                min: 0,
                max: 2,
                ticks: {
                                    color: '#e3e3e3'  // Cor clara para os valores do eixo X
                                },
                                grid: {
                                    color: 'rgba(227, 227, 227, 0.2)'  // Linhas do grid com tom claro
                                }
            },
            y: {
                beginAtZero: false,
                min: -1,
                max: 1,
                ticks: {
                                                    color: '#e3e3e3'  // Cor clara para os valores do eixo X
                                                },
                                                grid: {
                                                    color: 'rgba(227, 227, 227, 0.2)'  // Linhas do grid com tom claro
                                                }
            }
        }
    }
});

// Função para atualizar o gráfico com animação para a onda e o ponto fixo
function atualizarGrafico() {
    if (t <= duracaoSimulacao) {
        console.log("Atualizando gráfico, t = ", t);

        // Calcula os pontos da onda para o tempo atual
        let pontosOnda = calcularPontosOnda(t);
        chart.data.labels = pontosOnda.dadosX;  // Atualizamos os valores de x
        chart.data.datasets[0].data = pontosOnda.dadosY;  // Atualizamos os valores de y da onda

        // Calcula o valor de y no ponto fixo em x = 1
        let valorNoPonto = calcularValorNoPonto(t);
        chart.data.datasets[1].data = [{ x: 1, y: valorNoPonto }];  // Atualiza a posição do ponto fixo em y

        chart.update();

        t += passoTempo;  // Avançamos o tempo
        setTimeout(atualizarGrafico, 1000 / fps);  // Próximo frame da animação
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

    fetch(`/pontoNoTempo?id_simulacao=${id_simulacao}&tempo=${tempo}`)
        .then(response => {
                    if (!response.ok) {
                        throw new Error(`Erro na requisição: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    const posicaoY = data.posicaoY;
                    if (posicaoY !== null) {
                        chart.data.datasets[1].data = [{ x: 1, y: posicaoY }];

                        const { dadosX, dadosY } = calcularPontosOnda(tempo);
                        chart.data.labels = dadosX;
                        chart.data.datasets[0].data = dadosY;

                        chart.update();
                    } else {
                        alert("Ponto não encontrado para o tempo especificado.");
                    }
                })
                .catch(error => console.error("Erro ao buscar o ponto:", error));
}
