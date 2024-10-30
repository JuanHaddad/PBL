let pontos = /*[[${pontos}]]*/ [];  // Lista de pontos da onda, enviada do servidor

    // Configurando o gráfico com Chart.js
    var ctx = document.getElementById('graficoOnda').getContext('2d');
    var chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: pontos.map(p => p.x),  // Usamos os valores de X dos pontos
            datasets: [{
                label: 'Onda Transversal',
                data: pontos.map(p => p.y),  // Usamos os valores de Y dos pontos
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 2,
                fill: false
            }]
        },
        options: {
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