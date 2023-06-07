import {Component, OnInit} from '@angular/core';
import {selectUserInfo} from "../../../core/store";
import {Store} from "@ngrx/store";
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-inspection-dashboard',
    templateUrl: './inspection-dashboard.component.html',
    styleUrls: ['./inspection-dashboard.component.css']
})
export class InspectionDashboardComponent implements OnInit {
    inspectionOfficer: Boolean = false
    supervisorCharge: Boolean = false
    personalStats: any
    allStats: any;
    names: String
    roles: any[]

    constructor(private store$: Store<any>, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.store$.select(selectUserInfo)
            .subscribe((u) => {
                this.roles = u.roles;
                this.names = u.fullName;
                this.supervisorCharge = this.hasRole(['DI_OFFICER_CHARGE_READ'])
                this.inspectionOfficer = this.hasRole(['DI_INSPECTION_OFFICER_READ'])
                if (this.supervisorCharge || this.inspectionOfficer) {
                    this.loadPersonalStats()
                    this.loadAllStats()
                }
            });
    }

    printObject(dd) {
        //console.log(dd)
    }

    loadPersonalStats() {
        this.diService.loadPersonalDashboard()
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.personalStats = res.data
                    } else {
                        console.log(res.message)
                    }
                },
                error => {
                    console.log(error)
                }
            )

    }

    loadAllStats() {
        this.diService.loadAllDashboard()
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.allStats = res.data
                    } else {
                        console.log(res.message)
                    }
                },
                error => {
                    console.log(error)
                }
            )
    }

    // Check if role is in required privileges
    hasRole(privileges: string[]): Boolean {
        if (this.roles?.length > 0) {
            for (let role of this.roles) {
                for (let p of privileges) {
                    if (role == p) {
                        return true
                    }
                }
            }
        }
        return false
    }

}
