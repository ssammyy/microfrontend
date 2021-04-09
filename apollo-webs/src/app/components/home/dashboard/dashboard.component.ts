import {Component, OnInit, ViewChild} from '@angular/core';
import {ApexOptions, ChartComponent} from 'ng-apexcharts';

//
// export type ChartOptions = {
//   series: ApexAxisChartSeries;
//   chart: ApexChart;
//   dataLabels: ApexDataLabels;
//   plotOptions: ApexPlotOptions;
//   xaxis: any;
//   annotations: any;
//   grid: any;
//   yaxis: any;
//   colors: any;
//   toolbar: any;
//   stroke: ApexStroke;
//   markers: ApexMarkers;
//   tooltip: ApexTooltip;
// };

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  @ViewChild('chart') chart!: ChartComponent;
  public chartOptions: Partial<ApexOptions>;
  public chart1Options!: Partial<ApexOptions>;
  public chart2Options!: Partial<ApexOptions>;
  public chart3Options!: Partial<ApexOptions>;
  public commonOptions: Partial<ApexOptions> = {
    dataLabels: {
      enabled: false
    },
    stroke: {
      curve: 'straight'
    },
    // toolbar: {
    //   tools: {
    //     selection: false
    //   }
    // },
    markers: {
      size: 6,
      hover: {
        size: 10
      }
    },
    tooltip: {
      followCursor: false,
      theme: 'dark',
      x: {
        show: false
      },
      marker: {
        show: false
      },
      y: {
        title: {
          formatter(): string {
            return '';
          }
        }
      }
    },
    xaxis: {
      type: 'datetime'
    }
  };


  constructor() {
    this.chartOptions = {
      series: [
        {
          name: 'My-series',
          data: [10, 41, 35, 51, 49, 62, 69, 91, 148]
        }
      ],
      chart: {
        height: 350,
        type: 'bar'
      },
      xaxis: {
        categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep']
      }
    };
  }


  ngOnInit(): void {
    this.initCharts();
  }

  public initCharts(): void {
    this.chart1Options = {
      series: [
        {

          name: 'chart1',
          data: this.generateDayWiseTimeSeries(
            new Date('11 Feb 2017').getTime(),
            20,
            {
              min: 10,
              max: 60
            }
          )
        }
      ],
      chart: {
        id: 'fb',
        group: 'social',
        type: 'line',
        height: 160
      },
      colors: ['#008FFB'],
      yaxis: {
        tickAmount: 2,
        labels: {
          minWidth: 40
        }
      }
    };

    this.chart2Options = {
      series: [
        {

          name: 'chart2',
          data: this.generateDayWiseTimeSeries(
            new Date('11 Feb 2017').getTime(),
            20,
            {
              min: 10,
              max: 30
            }
          )
        }
      ],
      chart: {
        id: 'tw',
        group: 'social',
        type: 'line',
        height: 160
      },
      colors: ['#546E7A'],
      yaxis: {
        tickAmount: 2,
        labels: {
          minWidth: 40
        }
      }
    };

    this.chart3Options = {
      series: [
        {

          name: 'chart3',
          data: this.generateDayWiseTimeSeries(
            new Date('11 Feb 2017').getTime(),
            20,
            {
              min: 10,
              max: 60
            }
          )
        }
      ],
      chart: {
        id: 'yt',
        group: 'social',
        type: 'area',
        height: 160
      },
      colors: ['#00E396'],
      yaxis: {
        tickAmount: 2,
        labels: {
          minWidth: 40
        }
      }
    };
  }

  public generateDayWiseTimeSeries(baseval: any, count: any, yrange: any): any[] {
    let i = 0;
    const series = [];
    while (i < count) {
      const x = baseval;
      const y =
        Math.floor(Math.random() * (yrange.max - yrange.min + 1)) + yrange.min;

      series.push([x, y]);
      baseval += 86400000;
      i++;
    }
    return series;
  }


}
