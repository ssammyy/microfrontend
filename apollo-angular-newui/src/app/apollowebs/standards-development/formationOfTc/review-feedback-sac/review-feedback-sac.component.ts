import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Document} from "../../../../core/store/data/std/request_std.model";
import {FormationOfTcService} from "../../../../core/store/data/std/formation-of-tc.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NgxSpinnerService} from "ngx-spinner";
import {JustificationForTc} from "../../../../core/store/data/std/formation_of_tc.model";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import Swal from "sweetalert2";
import swal from "sweetalert2";
import {UserRegister} from "../../../../shared/models/user";
import {Department} from "../../../../core/store/data/std/std.model";
import {MasterService} from "../../../../core/store/data/master/master.service";
import {ActivatedRoute} from "@angular/router";
import {formatDate} from "@angular/common";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";

declare const $: any;

@Component({
    selector: 'app-review-feedback-spc',
    templateUrl: './review-feedback-sac.component.html',
    styleUrls: ['./review-feedback-sac.component.css']
})
export class ReviewFeedbackSacComponent implements OnInit {

    tasks: JustificationForTc[] = [];
    displayedColumns: string[] = ['subject', 'proposer', 'purpose', 'nameOfTC', 'status', 'actions'];
    displayedColumn: string[] = ['subject', 'proposer', 'purpose', 'nameOfTC', 'status', 'actions', 'approve', 'reject'];

    displayedColumnsC: string[] = ['subject', 'proposer', 'purpose', 'nameOfTC', 'status', 'view', 'actions'];

    dataSource!: MatTableDataSource<JustificationForTc>;
    dataSourceB!: MatTableDataSource<JustificationForTc>;
    dataSourceC!: MatTableDataSource<JustificationForTc>;

    dataSourceD!: MatTableDataSource<JustificationForTc>;


    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    loading = false;
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

    public userDetails!: UserRegister;
    public sacDetails!: UserRegister;
    public spcDetails!: UserRegister;
    selectedDepartment: string;

    public departments !: Department[];
    public departmentSelected !: Department[];

    dateFormat = "yyyy-MM-dd  hh:mm";
    language = "en";

    stdApproveOrRejectWithReason: FormGroup;

    selectedProposal: number;


    constructor(private formationOfTcService: FormationOfTcService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService,
                private formBuilder: FormBuilder,
                private masterService: MasterService,
                private route: ActivatedRoute,
                private standardDevelopmentService: StandardDevelopmentService,

    ) {
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)
    }
    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }

    ngOnInit(): void {
        this.getAllSacJustifications(true);
        this.getAllSacJustificationsApproved()
        this.getAllSacJustificationsRejected()
        this.stdApproveOrRejectWithReason = this.formBuilder.group({
            commentsSac: ['', Validators.required],
            id: ['', Validators.required],

        });
    }

    public getAllSacJustifications(pageRefresh: boolean): void {

        this.loadingText = "Retrieving Please Wait ...."

        this.loading = pageRefresh;
        this.SpinnerService.show()
        this.formationOfTcService.sacGetAllApprovedJustificationsBySpc().subscribe(
            (response: JustificationForTc[]) => {
                this.tasks = response;
                this.SpinnerService.hide()
                this.dataSource = new MatTableDataSource(this.tasks);

                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide()

            }
        );
    }

    public getAllSacJustificationsForWebsite(): void {
        this.SpinnerService.show()
        this.formationOfTcService.sacGetAllForWebsite().subscribe(
            (response: JustificationForTc[]) => {
                this.tasks = response;
                this.SpinnerService.hide()
                this.dataSourceD = new MatTableDataSource(this.tasks);

                this.dataSourceD.paginator = this.paginator;
                this.dataSourceD.sort = this.sort;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide()

            }
        );
    }

    public getAllSacJustificationsApproved(): void {
        this.SpinnerService.show()
        this.formationOfTcService.sacGetAllForWebsite().subscribe(
            (response: JustificationForTc[]) => {
                this.tasks = response;
                this.SpinnerService.hide()
                this.dataSourceB = new MatTableDataSource(this.tasks);

                this.dataSourceB.paginator = this.paginator;
                this.dataSourceB.sort = this.sort;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide()

            }
        );
    }


    public getAllSacJustificationsRejected(): void {
        this.SpinnerService.show()
        this.formationOfTcService.sacGetAllRejected().subscribe(
            (response: JustificationForTc[]) => {
                this.tasks = response;
                this.SpinnerService.hide()
                this.dataSourceC = new MatTableDataSource(this.tasks);

                this.dataSourceC.paginator = this.paginator;
                this.dataSourceC.sort = this.sort;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide()

            }
        );
    }


    applyFilter(event: Event) {
        const filterValue = (event.target as HTMLInputElement).value;
        this.dataSource.filter = filterValue.trim().toLowerCase();

        if (this.dataSource.paginator) {
            this.dataSource.paginator.firstPage();
        }
        if (this.dataSourceB.paginator) {
            this.dataSourceB.paginator.firstPage();
        }
        if (this.dataSourceC.paginator) {
            this.dataSourceC.paginator.firstPage();
        }
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

        this.getAllDocs(String(this.proposalRetrieved.id))
        this.getDepartmentName(String(this.proposalRetrieved.departmentId))
        this.getSelectedUser(proposal.hofId)
        this.getSelectedSpc(proposal.spcId)
        if (proposal.status == "7"||proposal.status == "6") {
            this.getSelectedSac(proposal.sacId)

        }


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

        if(mode ==="approve") {
            this.proposalRetrieved = task;
            button.setAttribute('data-target', '#recommendation');
            this.selectedProposal = this.proposalRetrieved.id
        }

        if(mode ==="reject") {
            this.proposalRetrieved = task;
            button.setAttribute('data-target', '#rejectRecommendation');
            this.selectedProposal = this.proposalRetrieved.id
        }
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

    public approveProposal(formDirective): void {
        if (this.stdApproveOrRejectWithReason.valid) {
            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-success'
                },
                buttonsStyling: false
            });

            swalWithBootstrapButtons.fire({
                title: 'Are you sure your want to approve this proposal?',
                text: 'You won\'t be able to reverse this!',
                icon: 'success',
                showCancelButton: true,
                confirmButtonText: 'Yes!',
                cancelButtonText: 'No!',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    this.SpinnerService.show();
                    this.formationOfTcService.approveJustificationSAC(this.stdApproveOrRejectWithReason.value).subscribe(
                        (response) => {
                            this.SpinnerService.hide();
                            swalWithBootstrapButtons.fire(
                                'Approved!',
                                'Proposal Successfully Approved!',
                                'success'
                            );
                            this.SpinnerService.hide();
                            this.hideModelB()
                            this.showToasterSuccess(response.httpStatus, 'Proposal Approved And Advertised To Website');
                            this.getAllSacJustifications(false);
                            this.getAllSacJustificationsForWebsite()
                            this.getAllSacJustificationsApproved()
                            this.getAllSacJustificationsRejected()
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
                    cancelButton: 'btn btn-success'
                },
                buttonsStyling: false
            });

            swalWithBootstrapButtons.fire({
                title: 'Are you sure your want to reject this proposal?',
                text: 'You won\'t be able to reverse this!',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes!',
                cancelButtonText: 'No!',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    this.SpinnerService.show();
                    this.formationOfTcService.rejectJustificationSAC(this.stdApproveOrRejectWithReason.value).subscribe(
                        (response) => {
                            this.SpinnerService.hide();
                            swalWithBootstrapButtons.fire(
                                'Rejected!',
                                'Proposal Successfully Rejected!',
                                'success'
                            );
                            this.SpinnerService.hide();
                            this.hideModel()
                            this.showToasterSuccess(response.httpStatus, 'Proposal Successfully Rejected');
                            this.getAllSacJustifications(false);
                            this.getAllSacJustificationsForWebsite()
                            this.getAllSacJustificationsApproved()
                            this.getAllSacJustificationsRejected()
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



    formatFormDate(date: Date) {
        return formatDate(date, this.dateFormat, this.language);
    }




}
