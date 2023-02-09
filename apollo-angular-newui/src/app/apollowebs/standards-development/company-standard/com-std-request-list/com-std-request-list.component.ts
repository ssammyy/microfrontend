import {
    Component,
    ElementRef, NgZone,
    OnDestroy,
    OnInit,
    QueryList,
    ViewChild,
    ViewChildren,
    ViewEncapsulation
} from '@angular/core';
import {NgxSpinnerService} from "ngx-spinner";
import {ReplaySubject, Subject} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {
    ComHodTasks,
    ComStdAction,
    ComStdRequest,
    UserEntity,
    UsersEntity
} from "../../../../core/store/data/std/std.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {DataTableDirective} from "angular-datatables";
import swal from "sweetalert2";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {take, takeUntil} from "rxjs/operators";
import {MatSelect} from "@angular/material/select";
import {IDropdownSettings} from "ng-multiselect-dropdown";
import {ListItem} from "ng-multiselect-dropdown/multiselect.model";

@Component({
  selector: 'app-com-std-request-list',
  templateUrl: './com-std-request-list.component.html',
  styleUrls: ['./com-std-request-list.component.css','../../../../../../node_modules/@ng-select/ng-select/themes/default.theme.css'],
  encapsulation: ViewEncapsulation.None
})
export class ComStdRequestListComponent implements OnInit,OnDestroy {
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();

    /** Subject that emits when the component has been destroyed. */
    protected _onDestroy = new Subject();
  public users !: UserEntity[] ;
    selectedUser: number;
  tasks: ComStdRequest[] = [];
  public actionRequest: ComStdRequest | undefined;
    public committeeFormGroup!: FormGroup;
    public uploadDraftFormGroup!: FormGroup;
    blob: Blob;
    public uploadedFiles:  FileList;
   public dropdownSettings: IDropdownSettings = {};
    dropdownList: any[] = [];

  constructor(
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
      public  zone: NgZone,
  ) { }



  ngOnInit(): void {

    this.getCompanyStandardRequest();
    this.getUserList();
      this.committeeFormGroup = this.formBuilder.group({
          name:['', Validators.required],
          names:['', Validators.required],
          requestId:['', Validators.required]
      });
      this.uploadDraftFormGroup = this.formBuilder.group({
          id: [],
          proposalId:[],
          justificationNo:[],
          title:[],
          scope:[],
          normativeReference:[],
          symbolsAbbreviatedTerms:[],
          clause:[],
          special:[],
          docName:[],
          standardNumber:[],
          requestNumber: [],
          requestId:[],
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

      this.dropdownSettings = {
          singleSelection: false,
          idField: 'email',
          textField: 'name',
          selectAllText: 'Select All',
          unSelectAllText: 'UnSelect All',
          itemsShowLimit: 5,
          allowSearchFilter: true
      };

      // this.websiteCtrl.setValue(this.users[1]);
      // this.filteredWebsites.next(this.users.slice());
      //
      // this.websiteFilterCtrl.valueChanges
      //     .pipe(takeUntil(this._onDestroy))
      //     .subscribe(() => {
      //         this.filterBanks();
      //     });


  }

    onItemSelect(item: ListItem) {
        console.log(item);
    }

    onSelectAll(items: any) {
        console.log(items);
    }

    // ngAfterViewInit() {
    //     this.setInitialValue();
    // }

    ngOnDestroy(): void {
        this.dtTrigger.unsubscribe();
        //this._onDestroy.next();
        //this._onDestroy.complete();
    }

    // protected setInitialValue() {
    //     this.filteredWebsites
    //         .pipe(take(1), takeUntil(this._onDestroy))
    //         .subscribe(() => {
    //             this.singleSelect.compareWith = (a: UserEntity, b: UserEntity) => a && b && a.id === b.id;
    //         });
    // }

    // protected filterBanks() {
    //     if (!this.users) {
    //         return;
    //     }
    //
    //     let search = this.websiteFilterCtrl.value;
    //     if (!search) {
    //         this.filteredWebsites.next(this.users.slice());
    //         return;
    //     } else {
    //         search = search.toLowerCase();
    //     }
    //
    //     this.filteredWebsites.next(
    //         this.users.filter(bank => bank.name.toLowerCase().indexOf(search) > -1)
    //     );
    // }

    showToasterSuccess(title:string,message:string){
        this.notifyService.showSuccess(message, title)

    }
    showToasterError(title:string,message:string){
        this.notifyService.showError(message, title)

    }
    showToasterWarning(title:string,message:string){
        this.notifyService.showWarning(message, title)

    }

  public getCompanyStandardRequest(): void{
    this.SpinnerService.show();
    this.stdComStandardService.getCompanyStandardRequest().subscribe(
        (response: ComStdRequest[])=> {
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

  public onOpenModal(task: ComStdRequest,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='assignProjectLeader'){
      this.actionRequest=task;
      button.setAttribute('data-target','#assignProjectLeader');
    }
      if (mode==='formJointCommittee'){
          this.actionRequest=task;
          button.setAttribute('data-target','#formJointCommittee');
      }
      if (mode==='uploadDraft'){
          this.actionRequest=task;
          button.setAttribute('data-target','#uploadDraft');
          this.uploadDraftFormGroup.patchValue(
              {
                  requestNumber: this.actionRequest.requestNumber,
                  requestId: this.actionRequest.id,
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

  //Assign Project Leader
  public onAssign(comStdAction: ComStdAction): void{
    this.SpinnerService.show();
    this.stdComStandardService.assignRequest(comStdAction).subscribe(
        (response: ComStdAction) => {
          this.SpinnerService.hide();
          console.log(response);
          this.getCompanyStandardRequest();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
    this.hideModalAssign();
  }

    formJointCommittee(): void {
        this.SpinnerService.show();
        console.log(this.committeeFormGroup.value);
        const valueString=this.committeeFormGroup.get("name").value.split(",")
        this.stdComStandardService.formJCommittee(this.committeeFormGroup.value,valueString).subscribe(
            (response) => {
                //console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Committed Prepared`);
                swal.fire({
                    title: 'Joint Committee Uploaded.',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success ',
                    },
                    icon: 'success'
                });
                this.getCompanyStandardRequest();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Saving Joint Committee`);
                alert(error.message);
            }
        );
        this.hideModalJC();
    }

  public getUserList(): void {
    this.SpinnerService.show();
    this.stdComStandardService.getUserList().subscribe(
        (response: UserEntity[]) => {
          this.SpinnerService.hide();
          this.users = response;
          //console.log(this.users)
          this.dropdownList = response;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  viewCommitmentLetter(pdfId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.stdComStandardService.viewCommitmentLetter(pdfId).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = fileName;
          link.click();
        },
        (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
            alert(error.message);
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

    uploadDraft(): void {
        this.SpinnerService.show();
        this.stdComStandardService.prepareCompanyPreliminaryDraft(this.uploadDraftFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getCompanyStandardRequest();
                this.uploadDraftFormGroup.reset();
                this.onClickSaveUploads(response.body.id)
                this.showToasterSuccess('Success', `Draft Uploaded`);
                this.SpinnerService.hide();

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalUploadDraft();
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
                        html:'Company Standard Draft Uploaded',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    }).then(r => this.uploadDraftFormGroup.reset());
                },
            );
        }

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
            }
        );
    }

    @ViewChild('closeModalAssign') private closeModalAssign: ElementRef | undefined;

    public hideModalAssign() {
        this.closeModalAssign?.nativeElement.click();
    }

    @ViewChild('closeModalJC') private closeModalJC: ElementRef | undefined;

    public hideModalJC() {
        this.closeModalJC?.nativeElement.click();
    }

    @ViewChild('closeModalUploadDraft') private closeModalUploadDraft: ElementRef | undefined;

    public hideModalUploadDraft() {
        this.closeModalUploadDraft?.nativeElement.click();
    }


}
