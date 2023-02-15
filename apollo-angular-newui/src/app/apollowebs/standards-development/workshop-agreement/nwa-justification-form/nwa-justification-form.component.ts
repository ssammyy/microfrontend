import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgxSpinnerService} from "ngx-spinner";
import {
    KNWCommittee,
    KNWDepartment,
    KnwSecTasks,
    NwaRequestList,
    UsersEntity
} from "../../../../core/store/data/std/std.model";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {StdNwaService} from "../../../../core/store/data/std/std-nwa.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {selectUserInfo} from "../../../../core/store";
import {Store} from "@ngrx/store";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";

declare const $: any;

@Component({
  selector: 'app-nwa-justification-form',
  templateUrl: './nwa-justification-form.component.html',
  styleUrls: ['./nwa-justification-form.component.css','../../../../../../node_modules/@ng-select/ng-select/themes/default.theme.css'],
    encapsulation: ViewEncapsulation.None
})


export class NwaJustificationFormComponent implements OnInit {
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    fullname = '';
  public itemId :string="1";
  public justification: string="Justification";
  public nwaDepartments !: KNWDepartment[];
  public nwaCommittees !: KNWCommittee[];
  public prepareJustificationFormGroup!: FormGroup;
  public knwsectasks !: KnwSecTasks[];
    public uploadedFiles: FileList;
    public knwSecretary !: UsersEntity[] ;
    nwaRequestLists: NwaRequestList[]=[];
    public actionRequests: NwaRequestList | undefined;
    loadingText: string;

  title = 'toaster-not';

  constructor(
      private store$: Store<any>,
      private router: Router,
      private formBuilder: FormBuilder,
      private stdNwaService: StdNwaService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    //this.getKNWDepartments();
    this.getKNWCommittee();
    //this.getKnwSecretary();
    this.getWorkshopStandards();

      this.store$.select(selectUserInfo).pipe().subscribe((u) => {
          return this.fullname = u.fullName;
      });



    this.prepareJustificationFormGroup = this.formBuilder.group({
        dateOfMeeting: ['', Validators.required],
      knw: ['', Validators.required],
      sl: ['', Validators.required],
        requestedBy: [],
        remarks: [],
        status: [],
      department: ['', Validators.required],
      issuesAddressed: ['', Validators.required],
        acceptanceDate: ['', Validators.required],
        uploadedFiles: [],
        requestId: [],
        DocDescription: []
      // postalAddress: ['', [Validators.required, Validators.pattern('P.O.BOX [0-9]{5}')]]
    });

  }

    ngOnDestroy(): void {
        this.dtTrigger.unsubscribe();
    }

  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }

  get formPrepareJustification(): any {
    return this.prepareJustificationFormGroup.controls;
  }

    public getWorkshopStandards(): void {
        this.loadingText = "Retrieving Requests...";
        this.SpinnerService.show();
        this.stdNwaService.getWorkshopStandards().subscribe(
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

  public getKNWDepartments(): void {
    this.stdNwaService.getKNWDepartments().subscribe(
        (response: KNWDepartment[]) => {
          this.nwaDepartments = response;
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
        }
    );
  }

  public getKNWCommittee(): void {
    this.stdNwaService.getKNWCommittee().subscribe(
        (response: KNWCommittee[]) => {
          this.nwaCommittees = response;
          //console.log(this.nwaCommittees)
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
        }
    );
  }

  saveJustification(): void {
    this.SpinnerService.show();
    this.stdNwaService.prepareJustification(this.prepareJustificationFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.SpinnerService.hide();
         //  this.showToasterSuccess(response.httpStatus, `Request Number is ${response.body.requestNumber}`);
         // this.onClickSaveUploads(response.body.savedRowID)
          this.prepareJustificationFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }

  public knwtasks(): void {
    this.stdNwaService.knwtasks().subscribe(
        (response: KnwSecTasks[]) => {
          this.knwsectasks = response;
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
        }
    );
  }

    onClickSaveUploads(nwaJustificationID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.stdNwaService.uploadFileDetails(nwaJustificationID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'Justification Prepared.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                     this.router.navigate(['/nwaJustification']);
                },
            );
        }

    }

    public getKnwSecretary(): void {
        this.SpinnerService.show();
        this.stdNwaService.getKnwSecretary().subscribe(
            (response: UsersEntity[]) => {
                this.SpinnerService.hide();
                this.knwSecretary = response;
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }


    showNotification(from: any, align: any) {
        const type = ['', 'info', 'success', 'warning', 'danger', 'rose', 'primary'];

        const color = Math.floor((Math.random() * 6) + 1);

        $.notify({
            icon: 'notifications',
            message: 'KEBS QAIMSS'
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

    public onOpenModal(nwaRequestList: NwaRequestList,mode:string): void{
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle','modal');
        if (mode==='prepareJustification'){
            this.actionRequests=nwaRequestList;
            button.setAttribute('data-target','#prepareJustification');
            this.prepareJustificationFormGroup.patchValue(
                {
                    requestId: this.actionRequests.id,
                    department: this.actionRequests.departmentId,
                    requestedBy: this.actionRequests.name
                }
            );
        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    @ViewChild('closeModalUploadJustification') private closeModalUploadJustification: ElementRef | undefined;

    public hideModalUploadDraft() {
        this.closeModalUploadJustification?.nativeElement.click();
    }

}
