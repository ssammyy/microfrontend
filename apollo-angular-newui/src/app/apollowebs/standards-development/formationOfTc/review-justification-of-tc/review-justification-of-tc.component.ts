import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Document} from "../../../../core/store/data/std/request_std.model";
import {FormationOfTcService} from "../../../../core/store/data/std/formation-of-tc.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {JustificationForTc} from "../../../../core/store/data/std/formation_of_tc.model";
import {MatTableDataSource} from "@angular/material/table";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import Swal from "sweetalert2";
import swal from "sweetalert2";
import {UserRegister} from "../../../../shared/models/user";
import {Department} from "../../../../core/store/data/std/std.model";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {MasterService} from "../../../../core/store/data/master/master.service";
import {ActivatedRoute} from "@angular/router";
import {formatDate} from "@angular/common";

@Component({
    selector: 'app-review-justification-of-tc',
    templateUrl: './review-justification-of-tc.component.html',
    styleUrls: ['./review-justification-of-tc.component.css']
})
export class ReviewJustificationOfTCComponent implements OnInit {

    tasks: JustificationForTc[] = [];

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
                private formBuilder: FormBuilder,
                private standardDevelopmentService: StandardDevelopmentService,
                private masterService: MasterService,
                private route: ActivatedRoute,
    ) {
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)
    }

    ngOnInit(): void {
        this.getAllSacJustifications(true);


        this.stdApproveOrRejectWithReason = this.formBuilder.group({
            commentsSpc: ['', Validators.required],
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

  public getAllSacJustifications(pageRefresh: boolean): void {

        this.loadingText = "Retrieving Please Wait ...."

        this.loading = pageRefresh;
        this.SpinnerService.show()
        this.formationOfTcService.getAllSpcJustifications().subscribe(
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

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

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

    public onOpenModal(task: JustificationForTc, mode: string): void {
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

    public reasonApprove(formDirective): void {
        if (this.stdApproveOrRejectWithReason.valid) {
            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                  cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
            });

            swalWithBootstrapButtons.fire({
                title: 'Are you sure your want to approve this proposal?',
                text: 'You won\'t be able to reverse this!',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes!',
                cancelButtonText: 'No!',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    this.SpinnerService.show();
                    this.formationOfTcService.approveJustificationSPCForTC(this.stdApproveOrRejectWithReason.value).subscribe(
                        (response) => {
                            this.SpinnerService.hide();
                            swalWithBootstrapButtons.fire(
                                'Approved!',
                                'Proposal Successfully Approved!',
                                'success'
                            );
                            this.SpinnerService.hide();
                            this.hideModelB()
                            this.showToasterSuccess(response.httpStatus, 'Proposal Successfully Approved');
                            this.getAllSacJustifications(false);

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

    public reasonReject(formDirective): void {
        if (this.stdApproveOrRejectWithReason.valid) {
            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                  cancelButton: 'btn btn-danger'
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
                    this.formationOfTcService.rejectJustificationSPC(this.stdApproveOrRejectWithReason.value).subscribe(
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
