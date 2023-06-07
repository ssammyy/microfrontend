import {Component, ElementRef, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {StdIntStandardService} from "../../../../../core/store/data/std/std-int-standard.service";
import {
    DocView,
    ISAdoptionComments,
    ISAdoptionProposal, PredefinedSDCommentsFields,
    ProposalComments
} from "../../../../../core/store/data/std/std.model";
import {NotificationService} from "../../../../../core/store/data/std/notification.service";
import {Store} from "@ngrx/store";
import {selectUserInfo} from "../../../../../core/store";
import {DefaulterDetails, DocumentDTO} from "../../../../../core/store/data/levy/levy.model";
import {DataTableDirective} from "angular-datatables";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {StdComStandardService} from "../../../../../core/store/data/std/std-com-standard.service";
import {PredefinedResourcesRequired} from "../../../../../core/store/data/ms/ms.model";

declare const $: any;

@Component({
  selector: 'app-int-std-comments',
  templateUrl: './int-std-comments.component.html',
  styleUrls: ['./int-std-comments.component.css']
})
export class IntStdCommentsComponent implements OnInit,OnDestroy {
  fullname = '';
  blob: Blob;
    submitted = false;
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  tasks: ProposalComments[] = [];
    selectedOption = '';
  isAdoptionProposals: ISAdoptionProposal[]=[];
    dataSaveResourcesRequired: PredefinedSDCommentsFields;
    dataSaveResourcesRequiredList: PredefinedSDCommentsFields[] = [];
    public uploadCommentsFormGroup!: FormGroup;
        predefinedSDCommentsDataAdded: boolean = false
  loadingText: string;
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false
  public actionRequest: ISAdoptionProposal | undefined;
    proposalId: string;
    commentId: string;
    documentDTOs: DocumentDTO[] = [];
    docDetails: DocView[] = [];
  constructor(
      private store$: Store<any>,
      private formBuilder: FormBuilder,
      private stdIntStandardService : StdIntStandardService,
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private activatedRoute: ActivatedRoute,
  ) { }

  ngOnInit(): void {

      this.activatedRoute.paramMap.subscribe(
          rs => {
              this.proposalId = rs.get('proposalId');
              this.commentId = rs.get('cid');

          },
      );
      this.getProposals(this.proposalId,this.commentId);
      //console.log(this.proposalId);

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
          emailOfRespondent:null,
          phoneOfRespondent:null,
          stakeHolderId:null


      });

    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.fullname = u.fullName;
    });
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

    public getProposals(proposalId: string,commentId: string): void{
        this.loadingText = "Retrieving Proposals ...."
        this.SpinnerService.show();
        this.stdIntStandardService.getProposals(proposalId,commentId).subscribe(
            (response: ISAdoptionProposal[]) => {
                this.isAdoptionProposals = response;
               // console.log(this.isAdoptionProposals);
                this.SpinnerService.hide();
                this.rerender();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);

            }
        );
    }

  // public getISProposals(): void{
  //   this.SpinnerService.show();
  //   this.stdIntStandardService.getISProposals().subscribe(
  //       (response: ProposalComments[])=> {
  //         this.tasks = response;
  //         this.dtTrigger.next();
  //         this.SpinnerService.hide();
  //       },
  //       (error: HttpErrorResponse)=>{
  //           this.SpinnerService.hide();
  //         alert(error.message);
  //       }
  //   );
  // }
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
  public onOpenModal(isAdoptionProposal: ISAdoptionProposal,mode:string,comStdDraftID: number): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='comment'){
      this.actionRequest=isAdoptionProposal;
      button.setAttribute('data-target','#commentModal');
        // this.stdIntStandardService.getDraftDocumentList(comStdDraftID).subscribe(
        //     (response: DocumentDTO[]) => {
        //         this.documentDTOs = response;
        //         this.SpinnerService.hide();
        //         //console.log(this.documentDTOs)
        //     },
        //     (error: HttpErrorResponse) => {
        //         this.SpinnerService.hide();
        //         //console.log(error.message);
        //     }
        // );
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
                nameOfRespondent: this.actionRequest.stName,
                emailOfRespondent:this.actionRequest.stEmail,
                phoneOfRespondent: this.actionRequest.stTelephone,
                stakeHolderId: this.actionRequest.stId

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
        // console.log(this.uploadCommentsFormGroup.value);
        this.stdIntStandardService.submitDraftComments(this.uploadCommentsFormGroup.value).subscribe(
            (response ) => {
               // console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Comment Submitted`);
                this.getProposals(this.proposalId,this.commentId);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalComment();
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



}
