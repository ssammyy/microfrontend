import {Component, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {ViewMessageComponent} from "./view-message/view-message.component";
import {DatePipe} from "@angular/common";

@Component({
    selector: 'app-message-dashboard',
    templateUrl: './message-dashboard.component.html',
    styleUrls: ['./message-dashboard.component.css']
})
export class MessageDashboardComponent implements OnInit {
    activeStatus = 'in'
    chartType: string = 'bar';
    requestStatus = ""
    exchangeDate: any = null
    exchangeFile: any = null
    documentData: any[]
    exchangeStats: any[]
    chartDatasets: any[] = []
    chartLabels: any[] = []

    public chartColors: Array<any> = [
        {
            backgroundColor: [
                'rgba(255, 99, 132, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(75, 192, 192, 0.2)',
                'rgba(153, 102, 255, 0.2)',
                'rgba(255, 159, 64, 0.2)'
            ],
            borderColor: [
                'rgba(255,99,132,1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
                'rgba(153, 102, 255, 1)',
                'rgba(255, 159, 64, 1)'
            ],
            borderWidth: 2,
        },
        {
            backgroundColor: [
                'rgba(255, 125, 158, 0.2)',
                'rgba(3, 111, 184, 0.2)',
                'rgba(255, 255, 137, 0.2)',
                'rgba(75, 192, 192, 0.2)',
                'rgba(126, 243, 243, 0.2)',
                'rgba(255, 210, 115, 0.2)'
            ],
            borderColor: [
                'rgba(255, 125, 158, 1)',
                'rgba(3, 111, 184, 1)',
                'rgba(255, 255, 137, 1)',
                'rgba(75, 192, 192, 1)',
                'rgba(126, 243, 243, 1)',
                'rgba(255, 210, 115, 1)'
            ],
            borderWidth: 2,
        },
    ];
    public chartOptions: any = {
        responsive: true,
        scales: {
            xAxes: [{
                stacked: true
            }],
            yAxes: [
                {
                    stacked: true
                }
            ]
        }
    };
    maxPages = 20
    currentPage = 0
    defaultPageSize = 20
    totalCount = 20
    settings = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [
                //  { name: 'editRecord', title: '<i class="btn btn-sm btn-primary">View More</i>' },
                {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No data found',
        columns: {
            transactionReference: {
                title: '#',
                type: 'string'
            },
            remoteReference: {
                title: 'Remote Reference',
                type: 'string'
            },
            fileType: {
                title: 'File Type',
                type: 'string'
            },
            filename: {
                title: 'File Name',
                type: 'string'
            },
            varField2: {
                title: 'File Size',
                type: 'string'
            },
            flowDirection: {
                title: 'Flow Direction',
                type: 'string'
            },

            responseMessage: {
                title: 'Response Message',
                type: 'string'
            },
            transactionCompletedDate: {
                title: 'Completed ON',
                type: 'string',
                valuePrepareFunction: (date) => {
                    if (date) {
                        return new DatePipe("En-Us").transform(date, 'dd-MM-yyyy HH:mm')
                    }
                    return "01-01-1970 00:00"
                },
            },
            responseStatus: {
                title: 'Successful',
                type: 'string',
                valuePrepareFunction: (status) => {
                    if (status === '00') {
                        return 'Yes'
                    }
                    return "No"
                },
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };

    constructor(private diService: DestinationInspectionService, private dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.loadStats()
        this.loadData()
    }

    toggleStatus(status) {
        if (status !== this.activeStatus) {
            this.activeStatus = status
            this.currentPage = 0
            this.loadData()
        }
    }

    searchMessages() {
        this.currentPage = 0
        this.loadData()
    }

    summarizeDataset() {
        this.chartDatasets = []
        let documentData = {}
        // Summarize data
        for (let d of this.exchangeStats) {
            let key = d.fileType + " " + d.flowDirection
            let data = {
                label: key,
                success: 0,
                failed: 0
            }
            if (documentData.hasOwnProperty(key)) {
                data = d[key]
                if (!data) {
                    data = {
                        label: key,
                        success: 0,
                        failed: 0
                    }
                }
            }
            if (d.responseStatus === 1) {
                if (d.totalDocuments) {
                    data.success += d.totalDocuments
                }
            } else {
                if (d.totalDocuments) {
                    data.failed += d.totalDocuments
                }
            }
            documentData[key] = data
        }
        // Add to chart dataset
        this.chartLabels = []
        let successData = []
        let failedData = []
        for (let k of Object.keys(documentData)) {
            let d = documentData[k]
            successData.push(d.success)
            failedData.push(d.failed)
            this.chartLabels.push(d.label)
        }
        // Success Count
        this.chartDatasets.push({
            data: successData,
            label: 'Successful Requests',
        })
        this.chartDatasets.push({
            data: failedData,
            label: 'Failed Requests',
        })
        console.log(this.chartDatasets)
    }

    loadStats() {
        this.diService.loadExchangeStats(this.exchangeDate)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.exchangeStats = res.data
                        this.summarizeDataset()
                    } else {
                        this.diService.showError(res.message, null)
                    }
                },
                error => this.diService.showError(error.message, null)
            )
    }

    loadData() {
        // console.log(this.activeStatus)
        let direction = this.activeStatus.toUpperCase()
        this.documentData = []
        // Load data
        this.diService.loadExchangeMessages(this.requestStatus, direction, this.exchangeFile, this.exchangeDate, this.currentPage, this.defaultPageSize)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.documentData = res.data
                        if (res.totalCount > 1000) {
                            this.totalCount = 1000
                        } else {
                            this.totalCount = res.totalCount
                        }
                    } else {
                        this.diService.showError(res.message, null)
                    }
                },
                error => {
                    this.diService.showError(error.message, null)
                }
            )
    }

    pageChange(pageIndex?: number) {
        console.log(pageIndex)
        if (pageIndex && pageIndex != (this.currentPage + 1)) {
            this.currentPage = pageIndex - 1;
            // console.log(pageIndex)
            this.loadData()
        }
    }

    chartClicked(e: any): void {
    }

    chartHovered(e: any): void {
    }

    showRecord(data: any) {
        this.dialog.open(ViewMessageComponent, {
            data: data
        })
    }

    onCustomAction(action) {
        switch (action.action) {
            case 'viewRecord':
                this.showRecord(action.data)
                break
        }
    }

}
