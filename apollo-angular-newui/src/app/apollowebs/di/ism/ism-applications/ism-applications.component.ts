import {Component, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-ism-applications',
    templateUrl: './ism-applications.component.html',
    styleUrls: ['./ism-applications.component.css']
})
export class IsmApplicationsComponent implements OnInit {
    displayedColumns = ["ucrNumber", "companyName", "firstName", "middleName", "lastName", "emailAddress", "requestApproved", "completed"]
    ismApplications: any[]
    activeStatus = 'new-requests'
    applicationStatus = 0;
    page: number = 0
    pageSize: number = 20

    constructor(private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.loadData()
    }

    toggleStatus(status) {
        switch (status) {
            case "rejected-requests":
                this.applicationStatus = 2
                break
            case "approved-requests":
                this.applicationStatus = 1
                break
            default:
                this.applicationStatus = 0
        }
    }

    loadData() {
        this.diService.getIsmApplications(this.applicationStatus, this.page, this.pageSize)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.ismApplications = res.data
                    } else {
                        this.diService.showError(res.message)
                    }
                },
                error => {
                    this.diService.showError(error.message)
                }
            )
    }

    viewConsignment(cdId) {

    }

    viewRequest(requestId) {

    }

}
