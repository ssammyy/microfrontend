import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgxSpinnerService} from "ngx-spinner";
import {KNWCommittee, KNWDepartment, KnwSecTasks} from "../../../../core/store/data/std/std.model";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {StdNwaService} from "../../../../core/store/data/std/std-nwa.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {selectUserInfo} from "../../../../core/store";
import {Store} from "@ngrx/store";

declare const $: any;

@Component({
  selector: 'app-nwa-justification-form',
  templateUrl: './nwa-justification-form.component.html',
  styleUrls: ['./nwa-justification-form.component.css']
})


export class NwaJustificationFormComponent implements OnInit {
    fullname = '';

  public itemId :string="1";
  public justification: string="Justification";
  public nwaDepartments !: KNWDepartment[];
  public nwaCommittees !: KNWCommittee[];
  public prepareJustificationFormGroup!: FormGroup;
  public knwsectasks !: KnwSecTasks[];
    public uploadedFiles: FileList;

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
    this.getKNWDepartments();
    this.getKNWCommittee();
    //this.knwtasks();

      this.store$.select(selectUserInfo).pipe().subscribe((u) => {
          return this.fullname = u.fullName;
      });

    this.prepareJustificationFormGroup = this.formBuilder.group({
      meetingDate: ['', Validators.required],
      knw: ['', Validators.required],
      sl: ['', Validators.required],
        requestedBy: [],
        remarks: [],
        status: [],
      department: ['', Validators.required],
      issuesAddressed: ['', Validators.required],
      knwAcceptanceDate: ['', Validators.required],
        uploadedFiles: [],
        DocDescription: []
      // postalAddress: ['', [Validators.required, Validators.pattern('P.O.BOX [0-9]{5}')]]
    });

  }

  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }

  get formPrepareJustification(): any {
    return this.prepareJustificationFormGroup.controls;
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
          this.showToasterSuccess(response.httpStatus, `Request Number is ${response.body.requestNumber}`);
         this.onClickSaveUploads(response.body.savedRowID)
          this.prepareJustificationFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }
  //   saveJustification() {
  //       const file = this.uploadedFiles;
  //       const formData = new FormData();
  //       for (let i = 0; i < file.length; i++) {
  //           console.log(file[i]);
  //           formData.append('docFile', file[i], file[i].name);
  //       }
  //       this.SpinnerService.show();
  //       this.stdNwaService.prepareJustification(this.prepareJustificationFormGroup.value, formData).subscribe(
  //           (data: any) => {
  //               this.SpinnerService.hide();
  //               this.showToasterSuccess(data.httpStatus, `Request Number is ${data.body.requestNumber}`);
  //               //console.log(data);
  //               this.prepareJustificationFormGroup.reset();
  //               swal.fire({
  //                   title: 'Justification Prepared.',
  //                   buttonsStyling: false,
  //                   icon: 'success'
  //               });
  //           },
  //           (error: HttpErrorResponse) => {
  //               this.SpinnerService.hide();
  //               console.log(error.message);
  //           }
  //       );
  //
  //   }

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


  // public saveJustification(saveJustification: NgForm): void{
  //   this.stdDevelopmentNwaService.prepareJustification(saveJustification.value).subscribe(
  //     (response:StandardTasks) =>{
  //       console.log(response);
  //     },
  //     (error:HttpErrorResponse) =>{
  //       alert(error.message);
  //     }
  //   )
  // }
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

}
