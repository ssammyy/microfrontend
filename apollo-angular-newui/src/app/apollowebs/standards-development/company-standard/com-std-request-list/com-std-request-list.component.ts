import {
    Component,
    ElementRef,
    OnDestroy,
    OnInit,
    QueryList,
    ViewChild,
    ViewChildren,
    ViewEncapsulation
} from '@angular/core';
import {NgxSpinnerService} from "ngx-spinner";
import {Subject} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {ComHodTasks, ComStdAction, ComStdRequest, UsersEntity} from "../../../../core/store/data/std/std.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {DataTableDirective} from "angular-datatables";
import swal from "sweetalert2";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

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
  public users !: UsersEntity[] ;
    selectedUser: number;
  tasks: ComStdRequest[] = [];
  public actionRequest: ComStdRequest | undefined;
    public committeeFormGroup!: FormGroup;
    public uploadDraftFormGroup!: FormGroup;
  blob: Blob;
  constructor(
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.getCompanyStandardRequest();
    this.getUserList();
      this.committeeFormGroup = this.formBuilder.group({
          name:['', Validators.required],
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
          requestId:[]

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
                  requestId: this.actionRequest.id
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
                console.log(response);
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
        (response: UsersEntity[]) => {
          this.SpinnerService.hide();
          this.users = response;
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
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Uploaded`);
                this.uploadDraftFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalUploadDraft();
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
