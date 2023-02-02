import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Subject} from "rxjs";
import {ISAdoptionProposal, ProposalComments} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DataTableDirective} from "angular-datatables";
import {Store} from "@ngrx/store";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {ActivatedRoute} from "@angular/router";
import {selectUserInfo} from "../../../../core/store";
import {HttpErrorResponse} from "@angular/common/http";

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
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ProposalComments[] = [];
  isAdoptionProposals: ISAdoptionProposal[] = [];
  public uploadCommentsFormGroup!: FormGroup;
  loadingText: string;
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false
  public actionRequest: ISAdoptionProposal | undefined;
  proposalId: string;

  constructor(
      private store$: Store<any>,
      private formBuilder: FormBuilder,
      private stdIntStandardService: StdIntStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService: NotificationService,
      private activatedRoute: ActivatedRoute,
  ) {
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe(
        rs => {
          this.proposalId = rs.get('proposalId');

        },
    );
    this.getSessionProposals(this.proposalId);
    //console.log(this.proposalId);

    this.uploadCommentsFormGroup = this.formBuilder.group({
      commentTitle: [],
      scope: [],
      clause: [],
      proposalID: [],
      standardNumber: [],
      commentDocumentType: [],
      recommendations: [],
      nameOfRespondent: [],
      positionOfRespondent: [],
      nameOfOrganization: [],
      preparedDate: [],
      paragraph: [],
      typeOfComment: [],
      comment: [],
      proposedChange: [],
      observation: [],
      emailOfRespondent: ['', Validators.required],
      phoneOfRespondent: ['', Validators.required]
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
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }

  public getSessionProposals(proposalId: string): void{
    this.loadingText = "Retrieving Proposals ...."
    this.SpinnerService.show();
    this.stdIntStandardService.getSessionProposals(proposalId).subscribe(
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
            commentTitle: this.actionRequest.title,
            scope: this.actionRequest.scope,
            clause: this.actionRequest.clause,
            proposalID: this.actionRequest.id,
            standardNumber: this.actionRequest.standardNumber,
            commentDocumentType: this.actionRequest.docName,
            preparedDate:this.actionRequest.preparedDate,
            nameOfRespondent:this.fullname,
            emailOfRespondent:this.email,
            nameOfOrganization:this.organization

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
    this.stdIntStandardService.submitProposalComments(this.uploadCommentsFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Comment Submitted`);
          this.getSessionProposals(this.proposalId);
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
