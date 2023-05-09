import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {StdIntStandardService} from "../../../../../core/store/data/std/std-int-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../../core/store/data/std/notification.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {
    ComStdContactFields,
    ISAdoptionProposal, NwaRequestList,
    StakeHoldersFields,
    UsersEntity
} from "../../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {selectUserInfo} from "../../../../../core/store";
import {ReplaySubject, Subject} from "rxjs";
import {MatSelect} from "@angular/material/select";
import {take, takeUntil} from "rxjs/operators";
import {IDropdownSettings} from "ng-multiselect-dropdown";
import {ListItem} from "ng-multiselect-dropdown/multiselect.model";
import {DataTableDirective} from "angular-datatables";

declare const $: any;

@Component({
  selector: 'app-is-proposal-form',
  templateUrl: './is-proposal-form.component.html',
  styleUrls: ['./is-proposal-form.component.css']
})
export class IsProposalFormComponent implements OnInit {
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    dtTrigger1: Subject<any> = new Subject<any>();
    urlRegex = /^(?:http(s)?:\/\/)?[\w.-]+(?:\.[\w\.-]+)+[\w\-\._~:/?#[\]@!\$&'\(\)\*\+,;=.]+$/;

    fullname = '';
  public uploadedFiles: FileList;
  public isProposalFormGroup!: FormGroup;
  title = 'toaster-not';
    proposal_doc_name: string;
    public stakeholdersLists : UsersEntity[]=[] ;
    public dropdownSettings: IDropdownSettings = {};
    dropdownList: UsersEntity[] = [];
    loadingText='';
    dataSaveResourcesRequired : StakeHoldersFields;
    dataSaveResourcesRequiredList: StakeHoldersFields[]=[];
    predefinedSDCommentsDataAdded: boolean = false;
    submitted = false;
    nwaRequestLists: NwaRequestList[]=[];
    public actionRequests: NwaRequestList | undefined;


  constructor(
      private store$: Store<any>,
      private formBuilder: FormBuilder,
      private stdIntStandardService : StdIntStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private router: Router,
  ) { }

  ngOnInit(): void {
      this.getIntStandardProposals();
      this.findStandardStakeholders();
      this.proposal_doc_name='Adoption Proposal'
    this.isProposalFormGroup = this.formBuilder.group({
        proposal_doc_name: null,
        uploadedBy: null,
        tcSecName : null,
        circulationDate : null,
        closingDate : null,
        title : null,
        scope : null,
        iStandardNumber:[null],
        stakeholdersList:null,
        stakeHolderName:null,
        stakeHolderEmail:null,
        stakeHolderPhone:null,
        requestId:null,
        adoptionProposalLink:['', [Validators.required, Validators.pattern(this.urlRegex)]],
        departmentId:null,
        departmentName:null,
        tcSecAssigned: null
        //addStakeholdersList:['', Validators.required]
        // adoptionAcceptableAsPresented : ['', Validators.required],
        // reasonsForNotAcceptance : ['', Validators.required],
        // recommendations : ['', Validators.required],
        // nameOfRespondent : ['', Validators.required],
        // positionOfRespondent : ['', Validators.required],
        // nameOfOrganization : ['', Validators.required],
        // dateOfApplication : ['', Validators.required],
    });

      this.store$.select(selectUserInfo).pipe().subscribe((u) => {
          return this.fullname = u.fullName;
      });

      this.dropdownSettings = {
          singleSelection: false,
          idField: 'email',
          textField: 'name',
          selectAllText: 'Select All',
          unSelectAllText: 'UnSelect All',
          allowSearchFilter: true
      };



  }

    onItemSelect(item: ListItem) {
        console.log(item);
    }

    onSelectAll(items: any) {
        console.log(items);
    }

    ngOnDestroy(): void {
        this.dtTrigger.unsubscribe();
        this.dtTrigger1.unsubscribe();
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

    public onOpenModal(nwaRequestList: NwaRequestList,mode:string): void{
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle','modal');
        if (mode==='prepareJustification'){
            this.actionRequests=nwaRequestList;
            button.setAttribute('data-target','#prepareJustification');
            this.isProposalFormGroup.patchValue(
                {
                    requestId: this.actionRequests.id,
                    title: this.actionRequests.subject,
                    scope: this.actionRequests.description,
                    proposal_doc_name: this.proposal_doc_name,
                    tcSecName: this.fullname,
                    departmentId: this.actionRequests.departmentId,
                    departmentName: this.actionRequests.departmentName,
                    tcSecAssigned:this.actionRequests.tcSecAssigned
                }
            );
        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

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



  get formISProposal(): any{
    return this.isProposalFormGroup.controls;
  }

  uploadProposal(): void {
    this.SpinnerService.show();
   // console.log(this.isProposalFormGroup.value);
    //const valueString=this.isProposalFormGroup.get("addStakeholdersList")
    //const valueString=this.isProposalFormGroup.get("addStakeholdersList").value.split(",")
    this.stdIntStandardService.prepareAdoptionProposal(this.isProposalFormGroup.value,this.dataSaveResourcesRequiredList).subscribe(
        (response) => {
          //console.log(response);
          this.SpinnerService.hide();
            this.showToasterSuccess(response.httpStatus, `Proposal Uploaded`);
          //this.onClickSaveUPLOADS(response.body.id)
          this.isProposalFormGroup.reset();
          this.getIntStandardProposals();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
            this.showToasterError('Error', `Error Preparing Proposal`);
          alert(error.message);
        }
    );
    this.hideModalUploadDraft();
  }

    onClickSaveUPLOADS(comStdDraftID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.loadingText = "Uploading Adoption Proposal Document...";
            this.SpinnerService.show();
            this.stdIntStandardService.uploadPDFileDetails(comStdDraftID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    swal.fire({
                        title: 'Adoption Proposal Document Uploaded.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    //this.router.navigate(['/nwaJustification']);
                },
            );

        }

    }


    onClickUploadISDocument(isProposalID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }

            this.SpinnerService.show();
            this.stdIntStandardService.uploadFileDetails(isProposalID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'International Standard Proposal Document Uploaded.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success ',
                        },
                        icon: 'success'
                    });
                    this.router.navigate(['/isProposalForm']);
                },
            );
        }

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

    // private b64Blob(strings: string[], type: any) {
    //     return undefined;
    // }
    //
    // private blobToFile(blob: Blob, test: string) {
    //
    // }

    public findStandardStakeholders(): void {
        this.SpinnerService.show();
        this.stdIntStandardService.findStandardStakeholders().subscribe(
            (response: UsersEntity[]) => {
                this.SpinnerService.hide();
                this.dropdownList = response;
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

    onClickAddResource() {
        this.dataSaveResourcesRequired = this.isProposalFormGroup.value;
        //console.log(this.dataSaveResourcesRequired);
        // tslint:disable-next-line:max-line-length
        this.dataSaveResourcesRequiredList.push(this.dataSaveResourcesRequired);
        //console.log(this.dataSaveResourcesRequiredList);
        this.predefinedSDCommentsDataAdded= true;
        //console.log(this.predefinedSDCommentsDataAdded);

        this.isProposalFormGroup?.get('stakeHolderName')?.reset();
        this.isProposalFormGroup?.get('stakeHolderEmail')?.reset();
        this.isProposalFormGroup?.get('stakeHolderPhone')?.reset();
    }

    removeDataResource(index) {
        console.log(index);
        if (index === 0) {
            this.dataSaveResourcesRequiredList.splice(index, 1);
            this.predefinedSDCommentsDataAdded = false
        } else {
            this.dataSaveResourcesRequiredList.splice(index, index);
        }
    }

    onClickSaveProposal() {
        this.submitted = true;
        console.log(this.dataSaveResourcesRequiredList.length);
        if (this.dataSaveResourcesRequiredList.length > 0) {
            this.uploadProposal();
        }
    }

    public getIntStandardProposals(): void {
        this.loadingText = "Retrieving Requests...";
        this.SpinnerService.show();
        this.stdIntStandardService.getIntStandardProposals().subscribe(
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
    @ViewChild('closeModalUploadJustification') private closeModalUploadJustification: ElementRef | undefined;

    public hideModalUploadDraft() {
        this.closeModalUploadJustification?.nativeElement.click();
    }

}
