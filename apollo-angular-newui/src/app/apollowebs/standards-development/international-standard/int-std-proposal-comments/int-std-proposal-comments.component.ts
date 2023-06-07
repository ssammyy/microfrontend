import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Subject} from "rxjs";
import {
    DocView,
    ISAdoptionProposal, ISComments,
    PredefinedSDCommentsFields,
    ProposalComments
} from "../../../../core/store/data/std/std.model";
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DataTableDirective} from "angular-datatables";
import {Store} from "@ngrx/store";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {ActivatedRoute} from "@angular/router";
import {selectUserInfo} from "../../../../core/store";
import {HttpErrorResponse} from "@angular/common/http";
import {DocumentDTO} from "../../../../core/store/data/levy/levy.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";

declare const $: any;

@Component({
  selector: 'app-int-std-proposal-comments',
  templateUrl: './int-std-proposal-comments.component.html',
  styleUrls: ['./int-std-proposal-comments.component.css']
})
export class IntStdProposalCommentsComponent implements OnInit {
  fullname = '';
  email = '';
  organization = '';
  blob: Blob;
    submitted = false;
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
    dtTrigger1: Subject<any> = new Subject<any>();
  tasks: ProposalComments[] = [];
  isAdoptionProposals: ISAdoptionProposal[] = [];
  public uploadCommentsFormGroup!: FormGroup;
  public uploadCommentFormGroup!: FormGroup;
  loadingText: string;
  isDtInitialized: boolean = false
  public actionRequest: ISAdoptionProposal | undefined;
  proposalId: string;
  documentDTOs: DocumentDTO[] = [];
  docDetails: DocView[] = [];
    isComments: ISComments[] = [];
    selectedOption = '';
    valueString:AbstractControl
    dataSaveResourcesRequired: PredefinedSDCommentsFields;
    dataSaveResourcesRequiredList: PredefinedSDCommentsFields[] = [];
    predefinedSDCommentsDataAdded: boolean = false

  constructor(
      private store$: Store<any>,
      private formBuilder: FormBuilder,
      private stdIntStandardService: StdIntStandardService,
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService: NotificationService,
      private activatedRoute: ActivatedRoute,
  ) {
  }

  ngOnInit(): void {

    this.getSessionProposals();

    this.uploadCommentsFormGroup = this.formBuilder.group({
        commentDocumentType: null,
        circulationDate: null,
        closingDate: null,
        standardNumber: null,
        commentTitle: null,
        scope: null,
        reasons: null,
        recommendations: null,
        nameOfRespondent: null,
        positionOfRespondent: null,
        nameOfOrganization: null,
        requestId: null,
        draftId: null,
        adoptionAcceptableAsPresented:null,
        stakeHolderId: null
    });

      this.uploadCommentFormGroup = this.formBuilder.group({
          commentDocumentType: null,
          circulationDate: null,
          closingDate: null,
          standardNumber: null,
          commentTitle: null,
          scope: null,
          reasons: null,
          recommendations: null,
          nameOfRespondent: null,
          positionOfRespondent: null,
          nameOfOrganization: null,
          requestId: null,
          draftId: null,
          adoptionAcceptableAsPresented:null,
          initialAdoptionAcceptableAsPresented:null,
          stakeHolderId: null,
          commentId: null
      });

    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.fullname = u.fullName;
    });

    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.email = u.email;
    });

    this.organization="KEBS"
  }

  get formUploadComment(): any {
    return this.uploadCommentsFormGroup.controls;
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
      this.dtTrigger1.unsubscribe();
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }

  public getSessionProposals(): void{
    this.loadingText = "Retrieving Proposals ...."
    this.SpinnerService.show();
    this.stdIntStandardService.getSessionProposals().subscribe(
        (response: ISAdoptionProposal[]) => {
          this.isAdoptionProposals = response;
          console.log(this.isAdoptionProposals);
          this.SpinnerService.hide();
            this.rerender();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);

        }
    );
  }

  viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.stdIntStandardService.viewProposalPDF(pdfId).subscribe(
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
          this.showToasterError('Error', `Error opening document`);
          alert(error.message);
        }
    );
  }

  public onOpenModal(isAdoptionProposal: ISAdoptionProposal,mode:string,commentId: number): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='comment'){
      this.actionRequest=isAdoptionProposal;
      button.setAttribute('data-target','#commentModal');

      this.uploadCommentsFormGroup.patchValue(
          {

              commentTitle: this.actionRequest.title,
              circulationDate:this.actionRequest.circulationDate,
              closingDate: this.actionRequest.closingDate,
              title:this.actionRequest.title,
              scope:this.actionRequest.scope,
              requestId: this.actionRequest.id,
              draftId: this.actionRequest.draftId,
              commentDocumentType: this.actionRequest.docName,
              standardNumber: this.actionRequest.standardNumber,
              nameOfRespondent:this.fullname,
              emailOfRespondent:this.email,
              nameOfOrganization:this.organization,
              stakeHolderId: this.actionRequest.stId

          }
      );
    }
      if (mode==='comments'){
          this.actionRequest=isAdoptionProposal;
          button.setAttribute('data-target','#commentsModal');
          this.stdIntStandardService.getProposalComments(commentId).subscribe(
              (response: ISComments[]) => {
                  this.isComments = response;
                  this.valueString=this.uploadCommentFormGroup.get("adoptionAcceptableAsPresented")
                  console.log(this.isComments);
              }
          );

          this.uploadCommentFormGroup.patchValue(
              {

                  commentTitle: this.actionRequest.title,
                  circulationDate:this.actionRequest.circulationDate,
                  closingDate: this.actionRequest.closingDate,
                  title:this.actionRequest.title,
                  scope:this.actionRequest.scope,
                  requestId: this.actionRequest.id,
                  draftId: this.actionRequest.draftId,
                  commentDocumentType: this.actionRequest.docName,
                  standardNumber: this.actionRequest.standardNumber,
                  nameOfRespondent:this.fullname,
                  emailOfRespondent:this.email,
                  nameOfOrganization:this.organization,
                  commentId:this.actionRequest.commentId,
                  stakeHolderId: this.actionRequest.stId,
                  adoptionAcceptableAsPresented:this.actionRequest.adoptionAcceptableAsPresented,
                  reasons:this.actionRequest.reasons,
                  recommendations:this.actionRequest.recommendations,
                  positionOfRespondent:this.actionRequest.positionOfRespondent

              }
          );
      }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

    onSubmit(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        //console.log(this.uploadCommentsFormGroup.value);
        this.stdIntStandardService.submitDraftComment(this.uploadCommentsFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Comment Submitted`);
                this.getSessionProposals();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalComment();
    }

    onSubmitChanges(): void {
        this.loadingText = "Updating Comment...";
        this.SpinnerService.show();
        this.stdIntStandardService.editSubmitDraftComment(this.uploadCommentFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Comment Updated`);
                this.getSessionProposals();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalComments();
    }
    proposals = [
        { value: "Yes", name: "Adoption acceptable as presented" },
        { value: "No", name: "Adoption proposal not acceptable because of the reason(s) below" }
    ];

    @ViewChild('closeModalComments') private closeModalComments: ElementRef | undefined;

    public hideModalComments() {
        this.closeModalComments?.nativeElement.click();
    }

  @ViewChild('closeModalComment') private closeModalComment: ElementRef | undefined;

  public hideModalComment() {
    this.closeModalComment?.nativeElement.click();
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
          '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
          '</div>' +
          '<a href="{3}" target="{4}" data-notify="url"></a>' +
          '</div>'
    });
  }

    // loadNotificationRead
    onClickAddResource() {
        this.dataSaveResourcesRequired = this.uploadCommentsFormGroup.value;
        console.log(this.dataSaveResourcesRequired);
        // tslint:disable-next-line:max-line-length
        this.dataSaveResourcesRequiredList.push(this.dataSaveResourcesRequired);
        console.log(this.dataSaveResourcesRequiredList);
        this.predefinedSDCommentsDataAdded= true;
        console.log(this.predefinedSDCommentsDataAdded);

        this.uploadCommentsFormGroup?.get('clause')?.reset();
        this.uploadCommentsFormGroup?.get('paragraph')?.reset();
        this.uploadCommentsFormGroup?.get('typeOfComment')?.reset();
        this.uploadCommentsFormGroup?.get('comment')?.reset();
        this.uploadCommentsFormGroup?.get('proposedChange')?.reset();
        this.uploadCommentsFormGroup?.get('observation')?.reset();
        this.uploadCommentsFormGroup?.get('scope')?.reset();
    }

    // Remove Form repeater values
    removeDataResource(index) {
        console.log(index);
        if (index === 0) {
            this.dataSaveResourcesRequiredList.splice(index, 1);
            this.predefinedSDCommentsDataAdded = false
        } else {
            this.dataSaveResourcesRequiredList.splice(index, index);
        }
    }

    onClickSaveWorkPlanScheduled() {
        this.submitted = true;
        console.log(this.dataSaveResourcesRequiredList.length);
        if (this.dataSaveResourcesRequiredList.length > 0) {
            this.onSubmit();
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
            this.dtTrigger1.next();
        });
    }
    onSelected(value:string): void {
        this.selectedOption = value;
    }

    public getProposalComments(draftId: number): void {
        this.loadingText = "Loading...";
        this.SpinnerService.show();
        this.stdIntStandardService.getProposalComments(draftId).subscribe(
            (response: ISComments[]) => {
                this.isComments = response;
                this.rerender();
                this.SpinnerService.hide();

            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );

    }

}
