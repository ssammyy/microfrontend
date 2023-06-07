import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NWAJustification, NwaRequestList, NwaTasks} from "../../../../core/store/data/std/std.model";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StdNwaService} from "../../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
declare const $: any;

@Component({
  selector: 'app-nwa-preliminary-draft',
  templateUrl: './nwa-preliminary-draft.component.html',
  styleUrls: ['./nwa-preliminary-draft.component.css']
})
export class NwaPreliminaryDraftComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  public approveReviewJustificationFormGroup!: FormGroup;
  public preparePreliminaryDraftFormGroup!: FormGroup;
  nwaRequestLists: NwaRequestList[]=[];
  nwaJustifications: NWAJustification[]=[];
  public actionRequests: NwaRequestList | undefined;
  blob: Blob;
  public uploadedFiles:  FileList;
  loadingText: string;

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
      workShopDate: []

    });
    this.getWorkshopForPDraft()
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
  public getWorkshopForPDraft(): void {
    this.loadingText = "Retrieving Requests...";
    this.SpinnerService.show();
    this.stdNwaService.getWorkshopForPDraft().subscribe(
        (response: NwaRequestList[]) => {
          this.nwaRequestLists = response;
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
          this.getWorkshopForPDraft();
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

  public onOpenModal(nwaRequestList: NwaRequestList,mode:string,requestId: number): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    this.getJustification(requestId)
    if (mode==='preparePD'){
      this.actionRequests=nwaRequestList;
      button.setAttribute('data-target','#preparePD');
      this.preparePreliminaryDraftFormGroup.patchValue(
          {
            requestId: this.actionRequests.id,
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
