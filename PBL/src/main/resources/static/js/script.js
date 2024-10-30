// Obtém os parâmetros da URL usando URLSearchParams
const duracaoSimulacao = parametrosSimulacao.duracao;
const frequencia = parametrosSimulacao.frequencia;
const comprimentoOnda = parametrosSimulacao.comprimentoOnda;
const erroMaximo = parametrosSimulacao.erroMaximo;

console.log("Duração da Simulação:", duracaoSimulacao);
console.log("Frequência:", frequencia);
console.log("Comprimento de Onda:", comprimentoOnda);
console.log("Erro Máximo:", erroMaximo);

// Variáveis de animação
let t = 0;  // Tempo inicial
const passoTempo = 0.0175;  // Passo de tempo (em segundos) NÃO MUDA ISSO DE FORMA ALGUMA
const fps = 60;  // Frames por segundo da animação

// Função para calcular o seno usando série de Taylor
function calcularSenoTaylor(valor, erroMaximo) {
    let termo = valor;  // Primeiro termo da série (x^1 / 1!)
    let soma = termo;
    let n = 1;

    while (Math.abs(termo) > erroMaximo) {
        termo *= -1 * valor * valor / ((2 * n) * (2 * n + 1));  // Termo da série
        soma += termo;
        n++;
    }

    return soma;
}

// Função para gerar os pontos da onda com base no tempo
function calcularPontosOnda(t) {
    let dadosX = [];
    let dadosY = [];
    const passoX = 0.01;  // Espaçamento entre pontos (1 cm)

    for (let x = 0; x <= 2; x += passoX) {
        // Fórmula da onda: y(x, t) = sin(2π(f * t - x / λ)) usando Taylor
        let argumento = 2 * Math.PI * (frequencia * t - x / comprimentoOnda);
        let y = calcularSenoTaylor(argumento, erroMaximo);
        dadosX.push(x);
        dadosY.push(y);
    }

    return { dadosX, dadosY };
}

// Função para calcular a posição do ponto fixo ao longo do comprimento de onda
function calcularPosicaoPonto(t) {
    // O ponto vai percorrer o gráfico de 0 a 1 conforme o comprimento de onda
    let posicaoX = (frequencia * comprimentoOnda) % 2;  // A posição vai "ciclar" de 0 a 1 repetidamente com o comprimento de onda
    return posicaoX;
}

// Função para calcular o valor da onda no ponto X para o tempo t
function calcularValorNoPonto(posicaoX, t) {
    // Fórmula da onda no ponto x = posicaoX
    let argumento = 2 * Math.PI * (frequencia * t - posicaoX / comprimentoOnda);
    let valorY = calcularSenoTaylor(argumento, erroMaximo);
    return valorY;
}

// Configurando o gráfico com Chart.js
var ctx = document.getElementById('graficoOnda').getContext('2d');
var chart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: [],  // Iniciamos vazio
        datasets: [{
            label: 'Onda Transversal',
            data: [],
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 2,
            fill: false
        },
        {
            label: 'Ponto em Movimento ao Longo da Onda',
            data: [{ x: 0, y: 0 }],  // Inicialmente em (x = 0, y = 0)
            borderColor: 'rgba(255, 0, 0, 1)',  // Ponto vermelho
            borderWidth: 5,
            showLine: false,  // Mostra apenas o ponto, sem linha conectada
            pointRadius: 5,
            pointBackgroundColor: 'red'
        }]
    },
    options: {
        animation: false,  // Desabilitamos a animação nativa, faremos manualmente
        scales: {
            x: {
                type: 'linear',
                position: 'bottom',
                min: 0,
                max: 2
            },
            y: {
                beginAtZero: false,
                min: -1,
                max: 1
            }
        }
    }
});

// Função para atualizar o gráfico com animação
function atualizarGrafico() {
    if (t <= duracaoSimulacao) {
        console.log("Atualizando gráfico, t = ", t);
        let pontosOnda = calcularPontosOnda(t);

        chart.data.labels = pontosOnda.dadosX;  // Atualizamos os valores de x
        chart.data.datasets[0].data = pontosOnda.dadosY;  // Atualizamos os valores de y

        // O ponto vermelho se move ao longo do comprimento de onda
        let posicaoX = calcularPosicaoPonto(t);  // O ponto se move de acordo com o comprimento de onda
        let valorNoPonto = calcularValorNoPonto(posicaoX, t);  // Calcula o valor de y no ponto x = posicaoX
        chart.data.datasets[1].data = [{ x: posicaoX, y: valorNoPonto }];  // Atualiza a posição do ponto

        chart.update();

        t += passoTempo;  // Avançamos o tempo
        setTimeout(atualizarGrafico, 1000 / fps);  // Próximo frame da animação
    } else {
        console.log("Animação finalizada no TEMPO(t) = ", t);
    }
}

// Iniciar a animação do gráfico
atualizarGrafico();
