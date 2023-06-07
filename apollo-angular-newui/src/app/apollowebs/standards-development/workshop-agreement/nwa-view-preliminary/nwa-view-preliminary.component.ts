import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {FormBuilder, FormGroup} from "@angular/forms";
import {
    ApproveDraft,
    COMPreliminaryDraft, DecisionOnStdDraft,
    DocView,
    NWAJustification,
    NwaRequestList
} from "../../../../core/store/data/std/std.model";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StdNwaService} from "../../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import {DocumentDTO} from "../../../../core/store/data/levy/levy.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import swal from "sweetalert2";

@Component({
  selector: 'app-nwa-view-preliminary',
  templateUrl: './nwa-view-preliminary.component.html',
  styleUrls: ['./nwa-view-preliminary.component.css']
})
export class NwaViewPreliminaryComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  public approveFormGroup!: FormGroup;
  tasks: COMPreliminaryDraft[]=[];
  nwaJustifications: NWAJustification[]=[];
  public actionRequest: COMPreliminaryDraft | undefined;
  decisionText: "";
  loadingText: string;
  blob: Blob;
  public uploadedFiles:  FileList;
  documentDTOs: DocumentDTO[] = [];
  docDetails: DocView[] = [];

  constructor(
      private store$: Store<any>,
      private router: Router,
      private formBuilder: FormBuilder,
      private stdNwaService: StdNwaService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private stdComStandardService:StdComStandardService
  ) { }

  ngOnInit(): void {
    this.getWorkShopStdDraft();
    this.approveFormGroup = this.formBuilder.group({
      comments: [],
      accentTo: [],
      id:[],
      requestId:[]

    });
  }

  get approveForm(): any {
    return this.approveFormGroup.controls;
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }

  public getWorkShopStdDraft(): void {
    this.loadingText = "Retrieving Requests...";
    this.SpinnerService.show();
    this.stdNwaService.getWorkShopStdDraft().subscribe(
        (response: COMPreliminaryDraft[]) => {
          this.tasks = response;
          this.rerender();
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }

  getJustification(id: number){
    this.loadingText = "Loading ...."
    this.SpinnerService.show();
    this.stdNwaService.getJustification(id).subscribe(
        (response: NWAJustification[]) => {
          this.nwaJustifications = response;
          this.SpinnerService.hide();
          //console.log(this.nwaJustifications)
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);
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
      this.dtTrigger.next();
    });
  }
  public getDraftDocumentList(comStdDraftID: number): void {
    this.loadingText = "Retrieving Requests...";
    this.SpinnerService.show();
    this.stdComStandardService.getDraftDocumentList(comStdDraftID).subscribe(
        (response: DocumentDTO[]) => {
          this.documentDTOs = response;
          this.SpinnerService.hide();
          //console.log(this.documentDTOs)
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          //console.log(error.message);
        }
    );
  }

  viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.stdComStandardService.viewCompanyDraft(pdfId).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});

          // tslint:disable-next-line:prefer-const
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = fileName;
          link.click();
          //console.log(this.blob);
          // this.pdfUploadsView = dataPdf;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Request`);
          console.log(error.message);
          //alert(error.message);
        }
    );
  }
  public onOpenModal(task: COMPreliminaryDraft,mode:string,draftId: number): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    this.getDraftDocumentList(draftId)
    if (mode==='approveDraft'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approveDraft');
      this.approveFormGroup.patchValue(
          {
            requestId: this.actionRequest.id,
            id: this.actionRequest.id,
            departmentId:this.actionRequest.departmentId,
            subject:this.actionRequest.subject,
            description:this.actionRequest.description,
            requestNumber:this.actionRequest.requestNumber,
            title:this.actionRequest.title
          }
      );
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  @ViewChild('closeModalUploadJustification') private closeModalUploadJustification: ElementRef | undefined;

  public hideModalUploadDraft() {
    this.closeModalUploadJustification?.nativeElement.click();
  }

    public decisionOnAccept(approveDraft: DecisionOnStdDraft): void{
        this.SpinnerService.show();
        this.stdNwaService.decisionOnDraft(approveDraft).subscribe(
            (response) => {
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, response.body.responseMessage);
                swal.fire({
                    text: response.body.responseMessage,
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'success'
                });
                this.getWorkShopStdDraft();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Processing Action`);
                console.log(error.message);
                this.getWorkShopStdDraft();
                //alert(error.message);
            }
        );
        this.hideModalUploadDraft();
    }



}
