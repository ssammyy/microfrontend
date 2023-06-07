import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {
    Document,
    NwiItem,
    StandardRequestB,
    StdJustification,
    StdJustificationDecision
} from "src/app/core/store/data/std/request_std.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {StandardDevelopmentService} from "src/app/core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "src/app/core/store/data/std/notification.service";
import {CommitteeService} from "src/app/core/store/data/std/committee.service";
import {formatDate} from "@angular/common";
import {UserRegister} from "src/app/shared/models/user";
import {ActivatedRoute} from "@angular/router";
import {MasterService} from "src/app/core/store/data/master/master.service";
import swal from "sweetalert2";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";

@Component({
  selector: 'app-approved-justification',
  templateUrl: './approved-justification.component.html',
  styleUrls: ['./approved-justification.component.css']
})
export class ApprovedJustificationComponent implements OnInit {

  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false

  public approved: StdJustification[] = [];
  public rejected: StdJustification[] = [];
  public rejectedWithAmendments: StdJustification[] = [];
  public actionRequest: StdJustification | undefined;


  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger1: Subject<any> = new Subject<any>();
  dtTrigger2: Subject<any> = new Subject<any>();
  dtTrigger3: Subject<any> = new Subject<any>();
  dtTrigger4: Subject<any> = new Subject<any>();
  dtTrigger5: Subject<any> = new Subject<any>();
  dtTrigger6: Subject<any> = new Subject<any>();

  public nwiItem!: NwiItem[];
  public stdJustificationDecision!: StdJustificationDecision[];
  docs !: Document[];
  blob: Blob;

  dateFormat = "yyyy-MM-dd";
  language = "en";

  tasks: StandardRequestB[] = [];

  p = 1;
  p2 = 1;
  public secTasks: NwiItem[] = [];
  public tscsecRequest !: NwiItem | undefined;
  public stdTSecFormGroup!: FormGroup;

  public formActionRequest: StdJustification | undefined;
  public userDetails!: UserRegister;
  public uploadedFiles: Array<File> = [];

  displayedColumns: string[] = ['sl', 'edition', 'title', 'spcMeetingDate', 'departmentId', 'actions'];
  dataSource!: MatTableDataSource<StdJustification>;
  dataSourceB!: MatTableDataSource<StdJustification>;
  dataSourceC!: MatTableDataSource<StdJustification>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;


  public itemId = "F&A/1:2021";
  public referenceMaterial: string = "ReferenceMaterialJustification";


  constructor(
      private formBuilder: FormBuilder,
      private standardDevelopmentService: StandardDevelopmentService,
      private SpinnerService: NgxSpinnerService,
      private notifyService: NotificationService,
      private committeeService: CommitteeService,
      private route: ActivatedRoute,
      private masterService: MasterService,
  ) {
  }

  ngOnInit(): void {
      this.getTCSECTasksJustification();
      this.getApprovedJustifications();
      this.getRejectedJustifications();
      this.getRejectedAmendmentJustifications();

  }


  get formStdTSec(): any {
      return this.stdTSecFormGroup.controls;
  }

  public getTCSECTasksJustification(): void {
      this.standardDevelopmentService.getAllNwiSApprovedForJustification().subscribe(
          (response: NwiItem[]) => {
              console.log(response);
              this.secTasks = response;
              this.rerender()
          },
          (error: HttpErrorResponse) => {
              alert(error.message);
          }
      )
  }

  public getApprovedJustifications(): void {
      this.standardDevelopmentService.getApprovedJustifications().subscribe(
          (response: StdJustification[]) => {
              // console.log(response);
              this.approved = response;
              this.dataSource = new MatTableDataSource(this.approved);
              this.dataSource.paginator = this.paginator;
              this.dataSource.sort = this.sort;
          },
          (error: HttpErrorResponse) => {
              alert(error.message);
          }
      )
  }


  public getRejectedJustifications(): void {
      this.standardDevelopmentService.getRejectedJustifications().subscribe(
          (response: StdJustification[]) => {
              // console.log(response);
              this.rejected = response;
              this.dataSourceB = new MatTableDataSource(this.rejected);
              this.dataSourceB.paginator = this.paginator;
              this.dataSourceB.sort = this.sort;
          },
          (error: HttpErrorResponse) => {
              alert(error.message);
          }
      )
  }

  public getRejectedAmendmentJustifications(): void {
      this.standardDevelopmentService.getRejectedAmendmentJustifications().subscribe(
          (response: StdJustification[]) => {
              // console.log(response);
              this.rejectedWithAmendments = response;
              this.dataSourceC = new MatTableDataSource(this.rejectedWithAmendments);
              this.dataSourceC.paginator = this.paginator;
              this.dataSourceC.sort = this.sort;
          },
          (error: HttpErrorResponse) => {
              alert(error.message);
          }
      )
  }


  showToasterSuccess(title: string, message: string) {
      this.notifyService.showSuccess(message, title)

  }

  uploadJustification(stdJustification: StdJustification): void {

      if (stdJustification.spcMeetingDate == '' ||
          stdJustification.departmentId == '' ||
          stdJustification.tcId == '' ||
          stdJustification.tcSecretary == '' ||
          stdJustification.sl == '' ||
          stdJustification.requestNo == '' ||
          stdJustification.title == '' ||
          stdJustification.edition == '' ||
          stdJustification.requestedBy == '' ||
          stdJustification.issuesAddressedBy == '' ||
          stdJustification.tcAcceptanceDate == '') {
          this.showToasterError("Error", "Please Fill In All The Details")

      } else {
          this.SpinnerService.show();
          this.standardDevelopmentService.uploadJustification(stdJustification).subscribe(
              (response) => {
                  this.showToasterSuccess(response.httpStatus, `Your Justification Has Been Uploaded`);
                  if (this.uploadedFiles.length > 0) {
                      this.uploadDocuments(response.body.savedRowID, "Relevant Documents")
                  } else {
                      this.SpinnerService.hide();
                      this.getTCSECTasksJustification();
                      this.hideModel();
                  }
              },
              (error: HttpErrorResponse) => {
                  this.SpinnerService.hide();

                  alert(error.message);
              }
          )
      }
  }

  public onOpenModal(secTask: NwiItem, mode: string): void {

      const container = document.getElementById('main-container');
      const button = document.createElement('button');
      button.type = 'button';
      button.style.display = 'none';
      button.setAttribute('data-toggle', 'modal');
      if (mode === 'edit') {
          console.log(secTask.taskId)
          this.tscsecRequest = secTask;
          this.itemId = this.tscsecRequest.id;
          this.getSelectedUser(secTask.tcSec)
          button.setAttribute('data-target', '#uploadSPCJustification');
      }

      // @ts-ignore
      container.appendChild(button);
      button.click();

  }

  public onOpenModalJustification(task: StdJustification, mode: string): void {
      const container = document.getElementById('main-container');
      const button = document.createElement('button');
      button.type = 'button';
      button.style.display = 'none';
      button.setAttribute('data-toggle', 'modal');
      console.log(task.taskId)
      if (mode === 'view') {
          this.actionRequest = task;
          this.getDecisionOnJustification(String(task.id))
          this.getSelectedUser(task.tcSecretary)
          this.getAllDocs(String(task.id), "Justification Documents")

          button.setAttribute('data-target', '#justificationDecisionModal');
      }
      // @ts-ignore
      container.appendChild(button);
      button.click();

  }

  public onOpenModalViewNwi(nwiId: string, mode: string): void {
      const container = document.getElementById('main-container');
      const button = document.createElement('button');
      button.type = 'button';
      button.style.display = 'none';
      button.setAttribute('data-toggle', 'modal');

      if (mode === 'edit') {
          this.getSpecificNwi(String(nwiId))
          this.getAllDocs(String(nwiId), "NWI Documents")
          button.setAttribute('data-target', '#viewNwi');
      }
      if (mode === 'viewRequest') {
          this.getSpecificStandardRequest(String(nwiId))
          this.getAllRequestDocs(String(nwiId))
          button.setAttribute('data-target', '#viewRequestModal');
      }


      // @ts-ignore
      container.appendChild(button);
      button.click();

  }

  @ViewChild('closeModal') private closeModal: ElementRef | undefined;
  @ViewChild('closeModalC') private closeModalC: ElementRef | undefined;


  public hideModel() {
      this.closeModal?.nativeElement.click();
  }

  public hideModelC() {
      this.closeModalC?.nativeElement.click();
  }


  public getSpecificNwi(nwiId: string): void {
      this.standardDevelopmentService.getNwiById(nwiId).subscribe(
          (response: NwiItem[]) => {
              this.nwiItem = response;

          },
          (error: HttpErrorResponse) => {
              alert(error.message);
          }
      )
  }

  public getDecisionOnJustification(justificationId: string): void {
      this.standardDevelopmentService.getJustificationDecisionById(justificationId).subscribe(
          (response: StdJustificationDecision[]) => {
              this.stdJustificationDecision = response;

          },
          (error: HttpErrorResponse) => {
              alert(error.message);
          }
      )
  }

  public getSpecificStandardRequest(standardRequestId: string): void {
      this.standardDevelopmentService.getRequestById(standardRequestId).subscribe(
          (response: StandardRequestB[]) => {
              this.tasks = response;

          },
          (error: HttpErrorResponse) => {
              alert(error.message);
          }
      )
  }

  public getAllDocs(nwiId: string, processName: string): void {
      this.standardDevelopmentService.getAdditionalDocumentsByProcess(nwiId, processName).subscribe(
          (response: Document[]) => {
              this.docs = response;
              this.rerender()


          },
          (error: HttpErrorResponse) => {
              alert(error.message);
          }
      );
  }

  rerender(): void {
      this.dtElements.forEach((dtElement: DataTableDirective) => {
          if (dtElement.dtInstance)
              dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                  dtInstance.destroy();
              });
      });
      setTimeout(() => {
          this.dtTrigger1.next();
          this.dtTrigger2.next();
          this.dtTrigger3.next();
          this.dtTrigger4.next();
          this.dtTrigger5.next();
          this.dtTrigger6.next();

      });

  }

  ngOnDestroy(): void {
      // Do not forget to unsubscribe the event
      this.dtTrigger1.unsubscribe();
      this.dtTrigger2.unsubscribe();
      this.dtTrigger3.unsubscribe();
      this.dtTrigger4.unsubscribe();
      this.dtTrigger5.unsubscribe();
      this.dtTrigger6.unsubscribe();

  }

  viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
      this.SpinnerService.show();
      this.committeeService.viewDocsById(pdfId).subscribe(
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

  public getAllRequestDocs(standardId: string): void {
      this.standardDevelopmentService.getAdditionalDocuments(standardId).subscribe(
          (response: Document[]) => {
              this.docs = response;
              this.rerender()


          },
          (error: HttpErrorResponse) => {
              alert(error.message);
          }
      );
  }


  formatFormDate(date: Date) {
      return formatDate(date, this.dateFormat, this.language);
  }

  private getSelectedUser(userId) {
      this.route.fragment.subscribe(params => {
          console.log(userId);
          this.masterService.loadUserDetails(userId).subscribe(
              (data: UserRegister) => {
                  this.userDetails = data;
              }
          );

      });
  }

  public uploadDocuments(justificationId: number, additionalInfo: string) {

      let file = null;

      if (additionalInfo == "Relevant Documents") {
          file = this.uploadedFiles;
      }

      if (file != null) {
          const formData = new FormData();
          for (let i = 0; i < file.length; i++) {
              console.log(file[i]);
              formData.append('docFile', file[i], file[i].name);
          }
          this.standardDevelopmentService.uploadFileDetailsJustification(String(justificationId), formData, additionalInfo, additionalInfo).subscribe(
              (data: any) => {
                  this.SpinnerService.hide();

                  this.uploadedFiles = [];


                  console.log(data);
                  swal.fire({
                      title: 'Uploaded Successfully.',
                      buttonsStyling: false,
                      customClass: {
                          confirmButton: 'btn btn-success form-wizard-next-btn ',
                      },
                      icon: 'success'
                  });
                  this.hideModel();
                  this.getTCSECTasksJustification();
                  this.SpinnerService.hide();


              },
          );
      }
  }

  showToasterError(title: string, message: string) {
      this.notifyService.showError(message, title)

  }

  applyFilter(event: Event) {
      const filterValue = (event.target as HTMLInputElement).value;
      this.dataSource.filter = filterValue.trim().toLowerCase();
      this.dataSourceB.filter = filterValue.trim().toLowerCase();
      this.dataSourceC.filter = filterValue.trim().toLowerCase();

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


}
