import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import swal from "sweetalert2";
import {LocalDataSource} from "ng2-smart-table";

@Component({
    selector: 'app-ministry-inspection-home',
    templateUrl: './ministry-inspection-home.component.html',
    styleUrls: ['./ministry-inspection-home.component.css']
})
export class MinistryInspectionHomeComponent implements OnInit {
    activeStatus: string = 'ongoing';
    public settings = {
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
            cdId: {
                title: '#',
                type: 'string'
            },
            cdUcr: {
                title: 'UCR No',
                type: 'string'
            },
            chassis: {
                title: 'Chassis Number',
                type: 'string'
            },
            used: {
                title: 'Used Indicator',
                type: 'string'
            },
            year: {
                title: 'Vehicle Year',
                type: 'string'
            },
            model: {
                title: 'Vehicle Model',
                type: 'string'
            },
            make: {
                title: 'Vehicle Make',
                type: 'string'
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    dataSet: LocalDataSource = new LocalDataSource();
    currentPage: number=0
    defaultPageSize: number=20
    totalCount: number=0

    constructor(private router: Router, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.loadData(0, 20);
    }

    pageChange(pageIndex?:number){
        if(pageIndex){
            this.currentPage=pageIndex-1;
            this.loadData(this.currentPage, this.defaultPageSize)
        }
    }

    private loadData(page: number, size: number): any {
        let status=1;
        if(this.activeStatus==='completed'){
            status=2
        }
        this.diService.listMinistryInspections(status, page, size)
            .subscribe(
                result => {
                    if (result.responseCode === "00") {
                        this.dataSet.load(result.data);
                    } else {
                        swal.fire({
                            title: result.message,
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'error'
                        }).then(() => {
                            console.log("data")
                        });
                    }
                }
            );
    }

    onCustomAction(event: any): void {
        switch (event.action) {
            case 'viewRecord':
                this.viewRecord(event.data);
                break;
        }
    }

    viewRecord(data: any) {
        this.router.navigate([`/ministry/inspection`, data.inspectionId]);
    }

    toggleStatus(status: string): void {
        if (this.activeStatus !== status) {
            this.activeStatus = status;
            this.loadData(0, 20)
        }
    }
}

