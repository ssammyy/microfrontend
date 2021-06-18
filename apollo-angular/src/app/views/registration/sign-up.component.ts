import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {
  BrsLookUpRequest,
  BrsLookUpRequestService,
  Company,
  loadResponsesFailure,
  RegistrationPayloadService,
  User
} from "../../core/store";
import {Store} from "@ngrx/store";
import {EntityOp} from "@ngrx/data";

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styles: []
})
export class SignUpComponent implements OnInit {

  step = 0;

  stepZeroForm: FormGroup = new FormGroup({});
  stepOneForm: FormGroup = new FormGroup({});
  stepTwoForm: FormGroup = new FormGroup({});
  stepThreeForm: FormGroup = new FormGroup({});
  stepFourForm: FormGroup = new FormGroup({});
  companySoFar: Partial<Company> | undefined;
  userSoFar: Partial<User> | undefined;


  constructor(
    private service: RegistrationPayloadService,
    private lookUpService: BrsLookUpRequestService,
    private store$: Store<any>,
    private brsLookupRequest: BrsLookUpRequest
  ) {
    service.getAll().subscribe();
  }

  ngOnInit(): void {
    this.stepZeroForm = new FormGroup({
      registrationNumber: new FormControl(),
      directorIdNumber: new FormControl(),
    });
    this.stepOneForm = new FormGroup({
      name: new FormControl(),
      registrationNumber: new FormControl('', [Validators.required]),
      kraPin: new FormControl('', [Validators.required]),
      yearlyTurnover: new FormControl('', [Validators.required]),
      directorIdNumber: new FormControl('', [Validators.required]),
      businessLines: new FormControl('', [Validators.required]),
      businessNatures: new FormControl('', [Validators.required]),
    });

    this.stepTwoForm = new FormGroup({
      postalAddress: new FormControl(),
      physicalAddress: new FormControl('', [Validators.required]),
      plotNumber: new FormControl('', [Validators.required]),
      companyEmail: new FormControl('', [Validators.required]),
      companyTelephone: new FormControl('', [Validators.required])
    });
    this.stepThreeForm = new FormGroup({
      buildingName: new FormControl(),
      streetName: new FormControl('', [Validators.required]),
      region: new FormControl('', [Validators.required]),
      county: new FormControl('', [Validators.required]),
      town: new FormControl('', [Validators.required])
    });
    this.stepFourForm = new FormGroup({
      firstName: new FormControl(),
      lastName: new FormControl('', [Validators.required]),
      userName: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required]),
      cellphone: new FormControl('', [Validators.required]),
      credentials: new FormControl('', [Validators.required]),
      confirmCredentials: new FormControl('', [Validators.required]),
    });
  }

  onClickBrsLookup(valid: boolean) {
    if (valid) {
      this.brsLookupRequest = this.stepZeroForm.value;

      console.log(`Sending ${JSON.stringify(this.brsLookupRequest)}`)
      this.step = 1;

    } else {
      this.store$.dispatch(loadResponsesFailure({
        error: {
          payload: 'Some required details are missing, kindly recheck',
          status: 100,
          response: '05'
        }
      }));
    }


  }

  onClickNext(valid: boolean) {
    if (valid) {
      switch (this.step) {
        case 1:
          this.companySoFar = this.stepOneForm?.value;
          break;
        case 2:
          this.companySoFar = {...this.companySoFar, ...this.stepTwoForm?.value};
          break;
        case 3:
          this.companySoFar = {...this.companySoFar, ...this.stepThreeForm?.value};
          break;
        case 4:
          this.userSoFar = this.stepFourForm?.value;
          break;
      }
      this.step += 1;
    }

  }
}
