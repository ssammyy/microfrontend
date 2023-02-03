import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {StdIntStandardService} from "../../../../../core/store/data/std/std-int-standard.service";
import {
    DocView,
    ISAdoptionComments,
    ISAdoptionProposal,
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

declare const $: any;

@Component({
  selector: 'app-int-std-comments',
  templateUrl: './int-std-comments.component.html',
  styleUrls: ['./int-std-comments.component.css']
})
export class IntStdCommentsComponent implements OnInit,OnDestroy {
  fullname = '';
  blob: Blob;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ProposalComments[] = [];
  isAdoptionProposals: ISAdoptionProposal[]=[];
    public uploadCommentsFormGroup!: FormGroup;
  loadingText: string;
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false
  public actionRequest: ISAdoptionProposal | undefined;
    proposalId: string;
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

          },
      );
      this.getProposals(this.proposalId);
      //console.log(this.proposalId);

      this.uploadCommentsFormGroup = this.formBuilder.group({
          commentTitle:[],
          scope:[],
          clause:[],
          proposalID:[],
          standardNumber:[],
          commentDocumentType:[],
          recommendations:[],
          nameOfRespondent:[],
          positionOfRespondent:[],
          nameOfOrganization:[],
          preparedDate:[],
          paragraph:[],
          typeOfComment:[],
          comment:[],
          proposedChange:[],
          observation:[],
          emailOfRespondent:['', Validators.required],
          phoneOfRespondent:['', Validators.required],
          draftID: [],
          requestID:[]

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
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }

    public getProposals(proposalId: string): void{
        this.loadingText = "Retrieving Proposals ...."
        this.SpinnerService.show();
        this.stdIntStandardService.getProposals(proposalId).subscribe(
            (response: ISAdoptionProposal[]) => {
                this.isAdoptionProposals = response;
                console.log(this.isAdoptionProposals);
                this.SpinnerService.hide();
                if (this.isDtInitialized) {
                    this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                        dtInstance.destroy();
                        this.dtTrigger.next();
                    });
                } else {
                    this.isDtInitialized = true
                    this.dtTrigger.next();
                }
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
        this.stdIntStandardService.getDraftDocumentList(comStdDraftID).subscribe(
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
        this.uploadCommentsFormGroup.patchValue(
            {
                commentTitle: this.actionRequest.title,
                requestID: this.actionRequest.id,
                draftID: this.actionRequest.draftId,
                commentDocumentType: this.actionRequest.docName,
                uploadDate: this.actionRequest.preparedDate,
                standardNumber: this.actionRequest.standardNumber

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
        console.log(this.uploadCommentsFormGroup.value);
        this.stdIntStandardService.submitDraftComments(this.uploadCommentsFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Comment Submitted`);
                this.getProposals(this.proposalId);
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



}
