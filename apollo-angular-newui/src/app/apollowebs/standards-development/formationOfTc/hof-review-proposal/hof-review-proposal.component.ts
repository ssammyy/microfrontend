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

@Component({
  selector: 'app-hof-review-proposal',
  templateUrl: './hof-review-proposal.component.html',
  styleUrls: ['./hof-review-proposal.component.css']
})
export class HofReviewProposalComponent implements OnInit {

  tasks: JustificationForTc[] = [];
  displayedColumns: string[] = ['subject', 'proposer', 'purpose', 'nameOfTC', 'status', 'actions'];
  displayedColumn: string[] = ['subject', 'proposer', 'purpose', 'nameOfTC', 'status', 'actions','approve','reject'];

  dataSource!: MatTableDataSource<JustificationForTc>;
  dataSourceB!: MatTableDataSource<JustificationForTc>;
  dataSourceC!: MatTableDataSource<JustificationForTc>;

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
    stdApproveOrRejectWithReason: FormGroup;

    selectedProposal: number;


  constructor(private formationOfTcService: FormationOfTcService,
              private notifyService: NotificationService,
              private SpinnerService: NgxSpinnerService,
              private formBuilder: FormBuilder,
  ) {
  }
  showToasterSuccess(title: string, message: string) {
    this.notifyService.showSuccess(message, title)
  }

  ngOnInit(): void {
    this.getAllHofJustifications(true);
    this.getAllHofJustificationsApproved()
    this.getAllHofJustificationsRejected()
      this.stdApproveOrRejectWithReason = this.formBuilder.group({
          comments: ['', Validators.required],
          id: ['', Validators.required],

      });
  }

  public getAllHofJustifications(pageRefresh: boolean): void {

    this.loadingText = "Retrieving Please Wait ...."

    this.loading = pageRefresh;
    this.SpinnerService.show()
    this.formationOfTcService.getAllHofJustifications().subscribe(
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

  public getAllHofJustificationsApproved(): void {
    this.SpinnerService.show()
    this.formationOfTcService.getAllSpcJustifications().subscribe(
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


  public getAllHofJustificationsRejected(): void {
    this.SpinnerService.show()
    this.formationOfTcService.getAllJustificationsRejectedBySpc().subscribe(
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

    this.getAllDocs(String(this.proposalRetrieved.id))


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
                title: 'Are you sure your want to approve this proposal?',
                text: 'You won\'t be able to reverse this!',
                icon: 'success',
                showCancelButton: true,
                confirmButtonText: 'Approve!',
                cancelButtonText: 'Reject!',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    this.SpinnerService.show();
                    this.formationOfTcService.approveJustificationForTC(this.stdApproveOrRejectWithReason.value).subscribe(
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
                            this.getAllHofJustifications(false);
                            this.getAllHofJustificationsApproved()
                            this.getAllHofJustificationsRejected()
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
                    this.formationOfTcService.rejectJustificationForTC(this.stdApproveOrRejectWithReason.value).subscribe(
                        (response) => {
                            this.SpinnerService.hide();
                            swalWithBootstrapButtons.fire(
                                'Rejected!',
                                'Proposal Successfully Rejected!',
                                'success'
                            );
                            this.SpinnerService.hide();
                            this.hideModelD()
                            this.showToasterSuccess(response.httpStatus, 'Proposal Successfully Rejected');
                            this.getAllHofJustifications(false);
                            this.getAllHofJustificationsApproved()
                            this.getAllHofJustificationsRejected()
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



}
