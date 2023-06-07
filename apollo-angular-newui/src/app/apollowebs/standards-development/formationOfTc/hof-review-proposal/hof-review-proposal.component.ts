import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {JustificationForTc} from "../../../../core/store/data/std/formation_of_tc.model";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {Document} from "../../../../core/store/data/std/request_std.model";
import {FormationOfTcService} from "../../../../core/store/data/std/formation-of-tc.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import Swal from "sweetalert2";
import swal from "sweetalert2";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {formatDate} from "@angular/common";
import {UserRegister} from "../../../../shared/models/user";
import {MasterService} from "../../../../core/store/data/master/master.service";
import {ActivatedRoute} from "@angular/router";
import {Department} from "../../../../core/store/data/std/std.model";

@Component({
    selector: 'app-hof-review-proposal',
    templateUrl: './hof-review-proposal.component.html',
    styleUrls: ['./hof-review-proposal.component.css']
})
export class HofReviewProposalComponent implements OnInit {

    tasks: JustificationForTc[] = [];loading = false;
    loadingText: string;
    proposalRetrieved: JustificationForTc;
    dtOptions: DataTables.Settings = {};
    dtOptionsB: DataTables.Settings = {};

    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger: Subject<any> = new Subject<any>();
    dtTrigger4: Subject<any> = new Subject<any>();

    docs !: Document[];
    blob: Blob;
    stdApproveOrRejectWithReason: FormGroup;

    selectedProposal: number;

    public userDetails!: UserRegister;
    selectedDepartment: string;


    dateFormat = "yyyy-MM-dd  hh:mm";
    language = "en";

    public departments !: Department[];
    public departmentSelected !: Department[];


    constructor(private formationOfTcService: FormationOfTcService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService,
                private standardDevelopmentService: StandardDevelopmentService,
                private masterService: MasterService,
                private route: ActivatedRoute,
                private formBuilder: FormBuilder,
    ) {
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)
    }

    ngOnInit(): void {
        this.getAllHofJustifications(true);

        this.stdApproveOrRejectWithReason = this.formBuilder.group({
            comments: ['', Validators.required],
            id: ['', Validators.required],

        });
    }

    id: any = 'New Proposals';

    tabChange(ids: any) {
        this.id = ids;
        if (this.id == "New Proposals") {
            this.standardDevelopmentService.reloadCurrentRoute()
        }
    }

    public getAllHofJustifications(pageRefresh: boolean): void {

        this.loadingText = "Retrieving Please Wait ...."

        this.loading = pageRefresh;
        this.SpinnerService.show()
        this.formationOfTcService.getAllHofJustifications().subscribe(
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


    public openModal(): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        button.setAttribute('data-target', '#requestModal');

        // @ts-ignore
        container.appendChild(button);
        button.click();
    }

    public onOpenModalDecision(task: JustificationForTc, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');

        if (mode === "approve") {
            this.proposalRetrieved = task;
            button.setAttribute('data-target', '#recommendation');
            this.selectedProposal = this.proposalRetrieved.id
        }

        if (mode === "reject") {
            this.proposalRetrieved = task;
            button.setAttribute('data-target', '#rejectRecommendation');
            this.selectedProposal = this.proposalRetrieved.id
        }
        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    @ViewChild('closeModalB') private closeModalB: ElementRef | undefined;

    public hideModelB() {
        this.closeModalB?.nativeElement.click();
    }

    @ViewChild('closeModalC') private closeModalC: ElementRef | undefined;

    public hideModelC() {
        this.closeModalC?.nativeElement.click();
    }

    @ViewChild('closeModalD') private closeModalD: ElementRef | undefined;

    public hideModelD() {
        this.closeModalD?.nativeElement.click();
    }

    get formApproveOrRejectWithReason(): any {
        return this.stdApproveOrRejectWithReason.controls;
    }

    public openModalToView(proposal: JustificationForTc): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        this.proposalRetrieved = proposal;

        button.setAttribute('data-toggle', 'modal');
        button.setAttribute('data-target', '#viewModal');

        this.getSelectedUser(proposal.createdBy)
        this.getDepartmentName(proposal.departmentId)

        // @ts-ignore
        container.appendChild(button);
        button.click();
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

    public getAllDocs(proposalId: string): void {
        this.formationOfTcService.getAdditionalDocuments(proposalId).subscribe(
            (response: Document[]) => {
                this.docs = response;
                this.rerender()


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }


    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.formationOfTcService.viewDocsById(pdfId).subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});

                // tslint:disable-next-line:prefer-const
                let downloadURL = window.URL.createObjectURL(this.blob);
                window.open(downloadURL, '_blank');

                // this.pdfUploadsView = dataPdf;
            },
        );
    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }


    public approveProposal(formDirective): void {
        if (this.stdApproveOrRejectWithReason.valid) {


            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
            });

            swalWithBootstrapButtons.fire({
                title: 'Are you sure your want to recommend this proposal?',
                text: 'You won\'t be able to reverse this!',
                icon: 'success',
                showCancelButton: true,
                confirmButtonText: 'Yes!',
                cancelButtonText: 'No!',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    this.loading = true
                    this.loadingText = "Recommending Proposal"
                    this.SpinnerService.show()
                    this.formationOfTcService.approveJustificationForTC(this.stdApproveOrRejectWithReason.value).subscribe(
                        (response) => {
                            this.SpinnerService.hide();
                            swalWithBootstrapButtons.fire(
                                'Recommended!',
                                'Proposal Successfully Recommended!',
                                'success'
                            );
                            this.SpinnerService.hide();
                            this.hideModelB()
                            this.showToasterSuccess(response.httpStatus, 'Proposal Successfully Recommended');
                            this.getAllHofJustifications(false);

                            formDirective.resetForm()
                        },
                    );
                } else if (
                    /* Read more about handling dismissals below */
                    result.dismiss === swal.DismissReason.cancel
                ) {
                    swalWithBootstrapButtons.fire(
                        'Cancelled',
                        'You have cancelled this operation',
                        'error'
                    );
                }
            });

        } else {
            this.showToasterError("Error", `Please Enter A Reason.`);

        }

    }

    public rejectProposal(formDirective): void {
        if (this.stdApproveOrRejectWithReason.valid) {
            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
            });

            swalWithBootstrapButtons.fire({
                title: 'Are you sure you do not want to recommend this proposal?',
                text: 'You won\'t be able to reverse this!',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes!',
                cancelButtonText: 'No!',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    this.loading = true
                    this.loadingText = "Not Recommending Proposal"
                    this.SpinnerService.show()
                    this.formationOfTcService.rejectJustificationForTC(this.stdApproveOrRejectWithReason.value).subscribe(
                        (response) => {
                            this.SpinnerService.hide();
                            swalWithBootstrapButtons.fire(
                                'Not Recommend!',
                                'Proposal Successfully Not Recommended!',
                                'success'
                            );
                            this.SpinnerService.hide();
                            this.hideModelD()
                            this.showToasterSuccess(response.httpStatus, 'Proposal Successfully Not Recommended');
                            this.getAllHofJustifications(false);

                            formDirective.resetForm()

                        },
                    );
                } else if (
                    /* Read more about handling dismissals below */
                    result.dismiss === swal.DismissReason.cancel
                ) {
                    swalWithBootstrapButtons.fire(
                        'Cancelled',
                        'You have cancelled this operation',
                        'error'
                    );
                }
            });

        } else {
            this.showToasterError("Error", `Please Enter A Reason.`);

        }

    }

    formatFormDate(date: Date) {
        return formatDate(date, this.dateFormat, this.language);
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
