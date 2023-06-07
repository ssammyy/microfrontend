import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DocView, NwaEditPd, NWAJustification, NwaRequestList} from "../../../../core/store/data/std/std.model";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StdNwaService} from "../../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {DocumentDTO} from "../../../../core/store/data/levy/levy.model";
declare const $: any;

@Component({
  selector: 'app-nwa-edit-preliminary-draft',
  templateUrl: './nwa-edit-preliminary-draft.component.html',
  styleUrls: ['./nwa-edit-preliminary-draft.component.css']
})
export class NwaEditPreliminaryDraftComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  public approveReviewJustificationFormGroup!: FormGroup;
  public preparePreliminaryDraftFormGroup!: FormGroup;
  //nwaRequestLists: NwaRequestList[]=[];
  nwaJustifications: NWAJustification[]=[];
  nwaPreliminaryEdits: NwaEditPd[]=[];
  public actionRequests: NwaEditPd | undefined;
  blob: Blob;
  public uploadedFiles:  FileList;
  loadingText: string;
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
    this.preparePreliminaryDraftFormGroup = this.formBuilder.group({
      title: ['', Validators.required],
      scope: ['', Validators.required],
      normativeReference: ['', Validators.required],
      symbolsAbbreviatedTerms: ['', Validators.required],
      clause: ['', Validators.required],
      special: ['', Validators.required],
      requestId: [],
      workShopDate: [],
      id:[]

    });
    this.getWorkShopDraftForEditing()
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
  get formPreparePD(): any {
    return this.preparePreliminaryDraftFormGroup.controls;
  }
  public getWorkShopDraftForEditing(): void {
    this.loadingText = "Retrieving Requests...";
    this.SpinnerService.show();
    this.stdNwaService.getWorkShopDraftForEditing().subscribe(
        (response: NwaEditPd[]) => {
          this.nwaPreliminaryEdits = response;
          this.rerender();
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }
  uploadPreliminaryDraft(): void {
    this.loadingText = "Saving...";
    this.SpinnerService.show();
    //console.log(this.preparePreliminaryDraftFormGroup.value);
    this.stdNwaService.preparePreliminaryDraft(this.preparePreliminaryDraftFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.SpinnerService.hide();
          this.getWorkShopDraftForEditing();
          this.showToasterSuccess(response.httpStatus, `Preliminary Draft  Uploaded`);
          this.onClickSaveUploads(response.body.id)
          this.preparePreliminaryDraftFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Preliminary Draft Was Not Prepared`);
          console.log(error.message);
        }
    );
    this.hideModalPreparePD()
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
  onClickSaveUploads(comStdDraftID: string) {
    if (this.uploadedFiles.length > 0) {
      const file = this.uploadedFiles;
      const formData = new FormData();
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
      this.SpinnerService.show();
      this.stdComStandardService.uploadPDFileDetails(comStdDraftID, formData).subscribe(
          (data: any) => {
            this.SpinnerService.hide();
            this.uploadedFiles = null;
            console.log(data);
            swal.fire({
              title: 'Thank you....',
              html:'Preliminary Draft Uploaded',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
              },
              icon: 'success'
            }).then(r => this.preparePreliminaryDraftFormGroup.reset());
          },
      );
    }

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

  public onOpenModal(nwaPreliminaryEdit: NwaEditPd,mode:string,requestId: number,draftId: number): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    this.getJustification(requestId)
    this.getDraftDocumentList(draftId)

    if (mode==='preparePD'){
      this.actionRequests=nwaPreliminaryEdit;
      button.setAttribute('data-target','#preparePD');
      this.preparePreliminaryDraftFormGroup.patchValue(
          {
            requestId: this.actionRequests.requestId,
            title: this.actionRequests.title,
            scope: this.actionRequests.scope,
            normativeReference: this.actionRequests.normativeReference,
            symbolsAbbreviatedTerms: this.actionRequests.symbolsAbbreviatedTerms,
            clause: this.actionRequests.clause,
            special: this.actionRequests.special,
            workShopDate: this.actionRequests.workShopDate,
            id: this.actionRequests.draftId
          }
      );
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  showNotification(from: any, align: any) {
    const type = ['', 'info', 'success', 'warning', 'danger', 'rose', 'primary'];

    const color = Math.floor((Math.random() * 6) + 1);

    $.notify({
      icon: 'notifications',
      message: 'Welcome to <b>Material Dashboard</b> - a beautiful dashboard for every web developer.'
    }, {
      type: type[color],
      timer: 3000,
      placement: {
        from: from,
        align: align
      },
      template: '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0} alert-with-icon" role="alert">' +
          '<button mat-raised-button type="button" aria-hidden="true" class="close" data-notify="dismiss">  <i class="material-icons">close</i></button>' +
          '<i class="material-icons" data-notify="icon">notifications</i> ' +
          '<span data-notify="title"></span> ' +
          '<span data-notify="message">Ensure all required fields and items have been filled</span>' +
          '<div class="progress" data-notify="progressbar">' +
          '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" ></div>' +
          '</div>' +
          '<a href="{3}" target="{4}" data-notify="url"></a>' +
          '</div>'
    });
  }
  @ViewChild('closeModalPreparePD') private closeModalPreparePD: ElementRef | undefined;

  public hideModalPreparePD() {
    this.closeModalPreparePD?.nativeElement.click();
  }

}
