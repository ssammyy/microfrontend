import {Component, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {ApprovedNwiS, Preliminary_Draft} from "../../../../../core/store/data/std/commitee-model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {
  DataHolder,
  Document,
  NwiItem, StandardRequestB,
  StdJustification,
  StdJustificationDecision
} from "../../../../../core/store/data/std/request_std.model";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {UserRegister} from "../../../../../shared/models/user";
import {IDropdownSettings} from "ng-multiselect-dropdown";
import {CommitteeService} from "../../../../../core/store/data/std/committee.service";
import {Store} from "@ngrx/store";
import {ActivatedRoute, Router} from "@angular/router";
import {NotificationService} from "../../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {MasterService} from "../../../../../core/store/data/master/master.service";
import {StandardDevelopmentService} from "../../../../../core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";
import {formatDate} from "@angular/common";

@Component({
  selector: 'app-all-tasks-pd',
  templateUrl: './all-tasks-pd.component.html',
  styleUrls: ['./all-tasks-pd.component.css']
})
export class AllTasksPdComponent implements OnInit {

  @Input() errorMsg: string;
  @Input() displayError: boolean;
  fullname = '';
  title = 'toaster-not';
  preliminary_draft: Preliminary_Draft | undefined;
  submitted = false;
  public preliminary_draftFormGroup!: FormGroup;
  public approvedNwiS !: ApprovedNwiS[];
  public approvedNwiSB !: ApprovedNwiS | undefined;
  public minutes_preliminary_draftFormGroup!: FormGroup;
  public approvedNwiSForPd !: NwiItem[];
  public approvedNwiSForPdB !: NwiItem | undefined;
  public uploadedFiles: FileList;
  public uploadedFilesB: FileList;
  public uploadedFilesC: FileList;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false
  validTextType: boolean = false;
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
  public justificationItem!: StdJustification[];
  public justificationItemB !: StdJustification | undefined;
  dateFormat = "yyyy-MM-dd";
  language = "en";
  displayedColumns: string[] = ['sl', 'edition', 'title', 'spcMeetingDate', 'departmentId', 'actions', '', ''];
  dataSource!: MatTableDataSource<StdJustification>;
  dataSourceB!: MatTableDataSource<StdJustification>;
  dataSourceC!: MatTableDataSource<StdJustification>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  public userDetails!: UserRegister;
  tasks: StandardRequestB[] = [];
  public allSdUsers !: UserRegister[];
  dropdownList: any[] = [];
  public tcs !: DataHolder[];
  public dropdownSettings: IDropdownSettings = {};
  public dropdownSettingsB: IDropdownSettings = {};
  selectedItems?: UserRegister;

  loading = false;
  loadingText: string;

  constructor(private formBuilder: FormBuilder,
              private committeeService: CommitteeService,
              private store$: Store<any>,
              private router: Router,
              private notifyService: NotificationService,
              private SpinnerService: NgxSpinnerService,
              private route: ActivatedRoute,
              private masterService: MasterService,
              private standardDevelopmentService: StandardDevelopmentService,
  )  { }

  ngOnInit(): void {

    this.getNWIS();

  }

  public getNWIS(): void {
    this.loading = true
    this.loadingText = "Retrieving Applications Please Wait ...."
    this.SpinnerService.show()
    this.committeeService.getAllNwiSForPd().subscribe(
        (response: NwiItem[]) => {
          this.loading = false
          this.approvedNwiSForPd = response;
          this.rerender()
          this.SpinnerService.hide();

        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();

          alert(error.message);
        }
    );

  }

  public onOpenModal(approvedNwiS: NwiItem, mode: string): void {

    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'edit') {
      this.approvedNwiSForPdB = approvedNwiS;
      button.setAttribute('data-target', '#updateNWIModal');
    }
    // if (mode === 'preparePd') {
    //     this.approvedNwiSForPdB = approvedNwiS;
    //     // button.setAttribute('data-target', '#preparePd');
    //     localStorage.setItem('id', approvedNwiS.id);
    //     // localStorage.removeItem(this.storageKey);
    //
    //
    //     this.router.navigate(['/prepareDraft'], { state: { id:approvedNwiS.id } });
    //
    // }
    if (mode === 'preparePd') {
      this.approvedNwiSForPdB = approvedNwiS;
      button.setAttribute('data-target', '#preparePd');
    }
    if (mode === 'uploadMinutes') {
      this.approvedNwiSForPdB = approvedNwiS;
      this.minutes_preliminary_draftFormGroup.controls.nwiId.setValue(approvedNwiS.id);

      button.setAttribute('data-target', '#uploadMinutes');
    }

    if (mode === 'uploadDraftsAndOtherRelevantDocuments') {
      this.approvedNwiSForPdB = approvedNwiS;
      button.setAttribute('data-target', '#uploadDraftsAndOtherRelevantDocuments');
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
    if (mode === 'justification') {
      this.getSpecificJustification(String(nwiId))
      button.setAttribute('data-target', '#justificationDecisionModal');
    }


    // @ts-ignore
    container.appendChild(button);
    button.click();

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

  public getSpecificJustification(nwiId: string): void {
    this.standardDevelopmentService.getJustificationByNwiId(nwiId).subscribe(
        (response: StdJustification[]) => {
          this.justificationItem = response;

          for (let product of response) {
            this.getDecisionOnJustification(product.id)
            this.getSelectedUser(product.tcSecretary)

          }

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




}
