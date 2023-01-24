import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
  ApproveDraft, ComJcJustificationDec,
  COMPreliminaryDraft,
  Department, InternationalStandardsComments,
  ISAdoptionProposal,
  ISJustificationProposal,
  StakeholderProposalComments
} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {DocumentDTO} from "../../../../core/store/data/levy/levy.model";

@Component({
  selector: 'app-com-std-editor',
  templateUrl: './com-std-editor.component.html',
  styleUrls: ['./com-std-editor.component.css']
})
export class ComStdEditorComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  tasks: COMPreliminaryDraft[] = [];
  public actionRequest: COMPreliminaryDraft | undefined;
  public departments !: Department[] ;
  stakeholderProposalComments: StakeholderProposalComments[] = [];
  internationalStandardsComments: InternationalStandardsComments[] = [];
  public isAdoption: ISAdoptionProposal | undefined;
  loadingText: string;
  approve: string;
  reject: string;
  isShowRemarksTab= true;
  isShowCommentsTab= true;
  isShowProposalTab=true;
  public uploadDraftStandardFormGroup!: FormGroup;
  documentDTOs: DocumentDTO[] = [];
  blob: Blob;
  constructor(
      private store$: Store<any>,
      private router: Router,
      private stdComStandardService:StdComStandardService,
      private standardDevelopmentService : StandardDevelopmentService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.getStdDraftForEditing();

    this.uploadDraftStandardFormGroup = this.formBuilder.group({
      id: [],
      requestId:[],
      title:[],
      scope:[],
      normativeReference:[],
      symbolsAbbreviatedTerms:[],
      clause:[],
      special:[],
      comStdNumber:[],
      preparedBy: [],
      docName:[],
      requestNumber:[],
        departmentId:[],
        subject:[],
        description:[],
        contactOneFullName:[],
        contactOneTelephone:[],
        contactOneEmail:[],
        contactTwoFullName:[],
        contactTwoTelephone:[],
        contactTwoEmail:[],
        contactThreeFullName:[],
        contactThreeTelephone:[],
        contactThreeEmail:[],
        companyName:[],
        companyPhone:[]

    });
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
  showToasterWarning(title:string,message:string){
    this.notifyService.showWarning(message, title)

  }
  public getStdDraftForEditing(): void{
    this.SpinnerService.show();
    this.stdComStandardService.getStdDraftForEditing().subscribe(
        (response: COMPreliminaryDraft[])=> {
          this.SpinnerService.hide();
          this.rerender();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  submitDraftForEditing(): void {
    this.loadingText = "Saving...";
    this.SpinnerService.show();
    this.stdComStandardService.submitDraftForEditing(this.uploadDraftStandardFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getStdDraftForEditing();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Draft Prepared`);

        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalDraftEditing();
  }

  public onOpenModal(task: COMPreliminaryDraft,mode:string,comStdDraftID: number): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
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
    if (mode==='draftStandardEditing') {
      this.actionRequest = task;
      button.setAttribute('data-target', '#draftStandardEditing');
      this.uploadDraftStandardFormGroup.patchValue(
          {
            id: this.actionRequest.id,
            requestId: this.actionRequest.requestId,
            requestNumber: this.actionRequest.requestNumber,
            title: this.actionRequest.title,
            scope:this.actionRequest.scope,
            normativeReference: this.actionRequest.normativeReference,
            symbolsAbbreviatedTerms: this.actionRequest.symbolsAbbreviatedTerms,
            clause:this.actionRequest.clause,
            special:this.actionRequest.special,
            comStdNumber:this.actionRequest.comStdNumber,
              departmentId:this.actionRequest.departmentId,
              subject:this.actionRequest.subject,
              description:this.actionRequest.description,
              contactOneFullName:this.actionRequest.contactOneFullName,
              contactOneTelephone:this.actionRequest.contactOneTelephone,
              contactOneEmail:this.actionRequest.contactOneEmail,
              contactTwoFullName:this.actionRequest.contactTwoFullName,
              contactTwoTelephone:this.actionRequest.contactTwoTelephone,
              contactTwoEmail:this.actionRequest.contactTwoEmail,
              contactThreeFullName:this.actionRequest.contactThreeFullName,
              contactThreeTelephone:this.actionRequest.contactThreeTelephone,
              contactThreeEmail:this.actionRequest.contactThreeEmail,
              companyName:this.actionRequest.companyName,
              companyPhone:this.actionRequest.companyPhone
          }
      );

    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

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
          // this.pdfUploadsView = dataPdf;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Request`);
          console.log(error.message);
          this.getStdDraftForEditing();
          //alert(error.message);
        }
    );
  }

  @ViewChild('closeModalDraftEditing') private closeModalDraftEditing: ElementRef | undefined;

  public hideModalDraftEditing() {
    this.closeModalDraftEditing?.nativeElement.click();
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

}
