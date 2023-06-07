import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {JustificationForTc} from "../../../../core/store/data/std/formation_of_tc.model";
import {HttpErrorResponse} from "@angular/common/http";
import {FormationOfTcService} from "../../../../core/store/data/std/formation-of-tc.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {FormBuilder} from "@angular/forms";
import {Store} from "@ngrx/store";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {MasterService} from "../../../../core/store/data/master/master.service";
import {ActivatedRoute} from "@angular/router";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {UserRegister} from "../../../../shared/models/user";
import {formatDate} from "@angular/common";
import {Department} from "../../../../core/store/data/std/std.model";

@Component({
    selector: 'app-approved-proposals',
    templateUrl: './approved-proposals.component.html',
    styleUrls: ['./approved-proposals.component.css']
})
export class ApprovedProposalsComponent implements OnInit {
    tasks: JustificationForTc[] = [];
    dtOptions: DataTables.Settings = {};
    dtOptionsB: DataTables.Settings = {};

    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger: Subject<any> = new Subject<any>();
    dtTrigger4: Subject<any> = new Subject<any>();
    proposalRetrieved: JustificationForTc;

    public departments !: Department[];
    public departmentSelected !: Department[];

    dateFormat = "yyyy-MM-dd  hh:mm";
    language = "en";
    public userDetails!: UserRegister;
    public sacDetails!: UserRegister;
    public spcDetails!: UserRegister;

    selectedDepartment: string;


    constructor(private formationOfTcService: FormationOfTcService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService,
                private formBuilder: FormBuilder,
                private store$: Store<any>,
                private standardDevelopmentService: StandardDevelopmentService,
                private masterService: MasterService,
                private route: ActivatedRoute,
    ) {


    }

    ngOnInit(): void {
        this.getAllHofJustificationsApproved()

    }

    public getAllHofJustificationsApproved(): void {
        this.SpinnerService.show()
        this.formationOfTcService.getAllApprovedJustifications().subscribe(
            (response: JustificationForTc[]) => {
                this.tasks = response;
                this.SpinnerService.hide()
                this.rerender()
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide()

            }
        );
    }


    rerender(): void {
        this.dtElements.forEach((dtElement: DataTableDirective) => {
            if (dtElement.dtInstance)
                dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                    dtInstance.clear();
                    dtInstance.destroy();
                });
        });
        setTimeout(() => {
            this.dtTrigger.next();
            this.dtTrigger4.next();
        });

    }

    public openModalApproved(proposal: JustificationForTc): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        this.proposalRetrieved = proposal;

        this.getSelectedUser(proposal.hofId)

        if (proposal.status == "4") {
            this.getSelectedSpc(proposal.spcId)
        }
        if (proposal.status == "6") {
            this.getSelectedSac(proposal.sacId)

        }

        button.setAttribute('data-toggle', 'modal');
        button.setAttribute('data-target', '#viewModal');

        this.getDepartmentName(String(this.proposalRetrieved.departmentId))


        // @ts-ignore
        container.appendChild(button);
        button.click();
    }



    formatFormDate(date: Date) {
        return formatDate(date, this.dateFormat, this.language);
    }

    public getDepartments(): void {
        this.standardDevelopmentService.getDepartmentsb().subscribe(
            (response: Department[]) => {
                this.departments = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }


    private getSelectedUser(userId) {
        this.route.fragment.subscribe(params => {
            this.masterService.loadUserDetails(userId).subscribe(
                (data: UserRegister) => {
                    this.userDetails = data;
                }
            );

        });
    }

    private getSelectedSpc(userId) {
        this.route.fragment.subscribe(params => {
            this.masterService.loadUserDetails(userId).subscribe(
                (data: UserRegister) => {
                    this.spcDetails = data;
                }
            );

        });
    }

    private getSelectedSac(userId) {
        this.route.fragment.subscribe(params => {
            this.masterService.loadUserDetails(userId).subscribe(
                (data: UserRegister) => {
                    this.sacDetails = data;
                }
            );

        });
    }

    public getDepartmentName(proposalId: string): void {
        this.standardDevelopmentService.getDepartmentById(proposalId).subscribe(
            (response: Department[]) => {
                this.departmentSelected = response;
                for (let h = 0; h < response.length; h++) {
                    this.selectedDepartment = response[h].name;
                }


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

}
