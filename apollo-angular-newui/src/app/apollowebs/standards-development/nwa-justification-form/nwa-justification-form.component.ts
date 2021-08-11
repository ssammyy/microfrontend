import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgxSpinnerService} from "ngx-spinner";
import {KNWCommittee, KNWDepartment, KnwSecTasks} from "../../../core/store/data/std/std.model";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {StdNwaService} from "../../../core/store/data/std/std-nwa.service";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {selectUserInfo} from "../../../core/store";
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
    this.knwtasks();

      this.store$.select(selectUserInfo).pipe().subscribe((u) => {
          return this.fullname = u.fullName;
      });

    this.prepareJustificationFormGroup = this.formBuilder.group({
      meetingDate: ['', Validators.required],
      knw: ['', Validators.required],
      sl: ['', Validators.required],
        requestedBy: [],
      department: ['', Validators.required],
      issuesAddressed: ['', Validators.required],
      knwAcceptanceDate: ['', Validators.required],
      knwSecretary: ['', Validators.required]
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
         this.onClickSaveULOADS(response.body.savedRowID)
          this.prepareJustificationFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
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

    onClickSaveULOADS(nwaJustificationID: string) {
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

}