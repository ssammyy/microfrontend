import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {ISAdoptionComments, ISAdoptionProposal, ProposalComments} from "../../../../core/store/data/std/std.model";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {Store} from "@ngrx/store";
import {selectUserInfo} from "../../../../core/store";
import {DefaulterDetails} from "../../../../core/store/data/levy/levy.model";
import {DataTableDirective} from "angular-datatables";
import {FormBuilder, FormGroup} from "@angular/forms";

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
  constructor(
      private store$: Store<any>,
      private formBuilder: FormBuilder,
      private stdIntStandardService : StdIntStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getProposal();

      this.uploadCommentsFormGroup = this.formBuilder.group({
          adoption_proposal_comment: [],
          commentTitle: [],
          commentDocumentType: [],
          comNameOfOrganization: [],
          comClause:[],
          comParagraph:[],
          proposedChange:[],
          taskId:[],
          proposalID:[],
          typeOfComment:[]

      });

    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.fullname = u.fullName;
    });
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

    public getProposal(): void{
        this.loadingText = "Retrieving Proposals ...."
        this.SpinnerService.show();
        this.stdIntStandardService.getProposal().subscribe(
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
  public onOpenModal(isAdoptionProposal: ISAdoptionProposal,mode:string): void{
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
                taskId: this.actionRequest.taskId,
                comNameOfOrganization: this.actionRequest.nameOfOrganization,
                commentTitle: this.actionRequest.title,
                scope: this.actionRequest.scope,
                comClause: this.actionRequest.clause,
                proposalID: this.actionRequest.id
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
        this.stdIntStandardService.submitAPComments(this.uploadCommentsFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Comment Submitted`);
                this.getProposal();
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



}
