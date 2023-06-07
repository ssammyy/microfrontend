import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {
  SuspendedCompanyDTO
} from "../../../core/store/data/levy/levy.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-standard-levy-suspension',
  templateUrl: './standard-levy-suspension.component.html',
  styleUrls: ['./standard-levy-suspension.component.css']
})
export class StandardLevySuspensionComponent implements OnInit {
  suspendedCompanyDTOs: SuspendedCompanyDTO[] = [];
  public actionRequest: SuspendedCompanyDTO | undefined;
  public approveRequestFormGroup!: FormGroup;
  public approveResumeFormGroup!: FormGroup;
  public rejectRequestFormGroup!: FormGroup;
  public rejectResumptionFormGroup!: FormGroup;

  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;

  dtTrigger: Subject<any> = new Subject<any>();
  isDtInitialized: boolean = false
  loadingText: string;

  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
      private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.getCompanySuspensionRequest();
    this.approveRequestFormGroup = this.formBuilder.group({
      id: [],
      companyId: [],
      reason: [],
      entryNumber: [],
      kraPin: [],
      registrationNumber: [],
      companyName: [],
      dateOfClosure: []

    });
    this.approveResumeFormGroup = this.formBuilder.group({
      id: [],
      companyId: [],
      reason: [],
      entryNumber: [],
      kraPin: [],
      registrationNumber: [],
      companyName: [],
      dateOfClosure: []

    });

    this.rejectRequestFormGroup = this.formBuilder.group({
      id: [],
      companyId: [],
      reason: [],
      entryNumber: [],
      kraPin: [],
      registrationNumber: [],
      companyName: [],
      dateOfClosure: []

    });
    this.rejectResumptionFormGroup = this.formBuilder.group({
      id: [],
      companyId: [],
      reason: [],
      entryNumber: [],
      kraPin: [],
      registrationNumber: [],
      companyName: [],
      dateOfClosure: []

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

  public getCompanySuspensionRequest(): void{
    this.loadingText = "Retrieving List ...."
    this.SpinnerService.show();
    this.levyService.getCompanySuspensionRequest().subscribe(
        (response: SuspendedCompanyDTO[]) => {
          this.suspendedCompanyDTOs = response;
          console.log(this.suspendedCompanyDTOs);
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

  confirmCompanySuspension(): void {
    this.loadingText = "Approving";
    this.SpinnerService.show();
    this.levyService.confirmCompanySuspension(this.approveRequestFormGroup.value).subscribe(
        (response) => {
         // console.log(response);
          this.getCompanySuspensionRequest();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Request for Suspension Approved`);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Approving Request`);
          console.log(error.message);
        }
    );
    this.hideModel();

  }

  confirmCompanyResumption(): void {
    this.loadingText = "Approving";
    this.SpinnerService.show();
    this.levyService.confirmCompanyResumption(this.approveResumeFormGroup.value).subscribe(
        (response) => {
          // console.log(response);
          this.getCompanySuspensionRequest();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Request for Resumption Approved`);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Approving Request`);
          console.log(error.message);
        }
    );
    this.hideModel();

  }

  rejectCompanySuspension(): void {
    this.loadingText = "Approving";
    this.SpinnerService.show();
    this.levyService.rejectCompanySuspension(this.rejectRequestFormGroup.value).subscribe(
        (response) => {
          //console.log(response);
          this.getCompanySuspensionRequest();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Request for Suspension Rejected`);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error try Again`);
          console.log(error.message);
        }
    );
    this.hideModel();

  }

  rejectCompanyResumption(): void {
    this.loadingText = "Approving";
    this.SpinnerService.show();
    this.levyService.rejectCompanyResumption(this.rejectResumptionFormGroup.value).subscribe(
        (response) => {
          //console.log(response);
          this.getCompanySuspensionRequest();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Request for Resumption Rejected`);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModel();

  }

  public openModal(suspendedCompanyDTOs: SuspendedCompanyDTO,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='viewDetails'){
      this.actionRequest=suspendedCompanyDTOs;
      button.setAttribute('data-target','#viewDetails');
      this.approveRequestFormGroup.patchValue(
          {
            id: this.actionRequest.id,
            companyId: this.actionRequest.companyId
          }
      );

    }
    if (mode==='viewResumption'){
      this.actionRequest=suspendedCompanyDTOs;
      button.setAttribute('data-target','#viewResumption');
      this.approveResumeFormGroup.patchValue(
          {
            id: this.actionRequest.id,
            companyId: this.actionRequest.companyId
          }
      );

    }
    if (mode==='rejectSuspension'){
      this.actionRequest=suspendedCompanyDTOs;
      button.setAttribute('data-target','#rejectSuspension');
      this.rejectRequestFormGroup.patchValue(
          {
            id: this.actionRequest.id,
            companyId: this.actionRequest.companyId
          }
      );

    }
    if (mode==='rejectResumption'){
      this.actionRequest=suspendedCompanyDTOs;
      button.setAttribute('data-target','#rejectResumption');
      this.rejectRequestFormGroup.patchValue(
          {
            id: this.actionRequest.id,
            companyId: this.actionRequest.companyId
          }
      );

    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  confirmRequest(): void {

  }

  @ViewChild('closeModal') private closeModal: ElementRef | undefined;
  public hideModel() {
    this.closeModal?.nativeElement.click();
  }

}
