import {Component, OnInit} from '@angular/core';
import {SystemService} from "../../../../core/store/data/system/system.service";
import {MatDialog} from "@angular/material/dialog";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-view-exemption-applications',
  templateUrl: './view-exemption-applications.component.html',
  styleUrls: ['./view-exemption-applications.component.css']
})
export class ViewExemptionApplicationsComponent implements OnInit {

  applicationStatus: any = "new"
  exemptionListing: any[]
  displayedColumns = ["serialNo", "conpanyName","companyPinNo","telephoneNo", "contactName", "contactEmail", "companyEmail","physicalLocation", "actions"]
  displayedColumnResults = ["serialNo", "conpanyName","companyPinNo","telephoneNo", "contactName", "contactEmail", "companyEmail","physicalLocation", "actions"]
  page = 0
    keywords: string | null = ""
    pageSize = 20
    currentPageInternal: number = 1

    constructor(private systemService: SystemService, private dialog: MatDialog, private pvocService: PVOCService, private router: Router) {
    }

    ngOnInit(): void {
        this.loadData()
    }

    toggleStatus(status: string): void {
        if (status !== this.applicationStatus) {
            this.applicationStatus = status;
            this.page = 0
            this.keywords = null
            this.currentPageInternal = 0
            this.loadData()
        }
    }

    searchPhraseChanged() {
        if (this.keywords && this.keywords.length > 0) {
            this.loadData()
        }
    }

    loadData() {
        this.pvocService.listExemptionApplications(this.keywords, this.applicationStatus, this.page, this.pageSize)
            .subscribe(
            res => {
              if (res.responseCode === "00") {
                this.exemptionListing = res.data
              } else {
                this.pvocService.showError(res.message, null)
              }
            },
            error => {
              this.pvocService.showError("Failed to load data with: " + error.message, null)
            }
        )
  }

    viewExemption(recordId: any) {
    this.router.navigate(["/pvoc/exemption/view",recordId])
  }

}
