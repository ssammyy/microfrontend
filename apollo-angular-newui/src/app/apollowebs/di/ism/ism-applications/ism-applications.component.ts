import {Component, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";

@Component({
    selector: 'app-ism-applications',
    templateUrl: './ism-applications.component.html',
    styleUrls: ['./ism-applications.component.css']
})
export class IsmApplicationsComponent implements OnInit {
    displayedColumns = ["ucrNumber", "companyName", "firstName", "middleName", "lastName", "emailAddress", "requestApproved", "completed","actions"]
    ismApplications: any[]
    activeStatus = 'new-requests'
    applicationStatus = 0;
    page: number = 0
    pageSize: number = 20

    constructor(private diService: DestinationInspectionService, private router: Router) {
    }

    ngOnInit(): void {
        this.loadData()
    }

    toggleStatus(status) {
        if (status === this.activeStatus) {
            return
        }
        this.activeStatus = status
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
        this.loadData()
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


    viewRequest(requestId) {
        this.router.navigate(["/di/ism/request",requestId])
    }

}
