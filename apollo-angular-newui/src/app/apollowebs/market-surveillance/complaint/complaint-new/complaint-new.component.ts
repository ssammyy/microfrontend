import {Component, OnInit} from '@angular/core';
import {interval, Observable, PartialObserver, Subject, throwError} from 'rxjs';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {
  BrsLookUpRequest,
  BusinessLines,
  BusinessLinesService,
  BusinessNatures,
  BusinessNaturesService,
  Company,
  County,
  CountyService, Go,
  loadBrsValidations,
  loadCountyId,
  loadRegistrations,
  loadResponsesFailure, loadSendTokenToPhone,
  loadValidateTokenAndPhone,
  Region,
  RegionService,
  RegistrationPayloadService,
  selectBrsValidationCompany,
  selectBrsValidationStep,
  selectCountyIdData,
  selectRegistrationStateSucceeded, selectTokenSentStateOtpSent,
  selectValidateTokenAndPhoneValidated,
  Town,
  TownService,
  User,
} from '../../../../core/store';
import {select, Store} from '@ngrx/store';
import {takeUntil} from 'rxjs/operators';
import {ConfirmedValidator} from '../../../../core/shared/confirmed.validator';
import {
  ComplaintCustomersDto,
  ComplaintDto,
  ComplaintLocationDto,
  MSComplaintSubmittedSuccessful,
  NewComplaintDto, SampleSubmissionDto,
} from '../../../../core/store/data/ms/ms.model';
import {MsService} from '../../../../core/store/data/ms/ms.service';
import {NgxSpinnerService} from 'ngx-spinner';


@Component({
  selector: 'app-complaint-new',
  templateUrl: './complaint-new.component.html',
  styleUrls: ['./complaint-new.component.css'],
})
export class ComplaintNewComponent implements OnInit {

  ispause = new Subject();
  time = 59;
  timer!: Observable<number>;
  timerObserver!: PartialObserver<number>;
  step = 4;
  regionName: string;
  countyName: string;
  townName: string;
  pdfUrls: string[] = [];

  public clicked = false;
  clickedInstructions = true;
  stepOneForm!: FormGroup;
  stepTwoForm!: FormGroup;
  stepThreeForm!: FormGroup;
  complaintCustomerSoFar: Partial<ComplaintCustomersDto> | undefined;
  complaintSoFar: Partial<ComplaintDto> | undefined;
  complaintLocationSoFar: Partial<ComplaintLocationDto> | undefined;

  stepZeroForm!: FormGroup;
  stepFourForm!: FormGroup;
  stepFiveForm!: FormGroup;

  uploadedFiles: FileList;
  arrayOfUploadedFiles = [];

  savedDetails: MSComplaintSubmittedSuccessful;
  customerDetails: ComplaintCustomersDto;
  complaintDetails: ComplaintDto;
  locationDetails: ComplaintLocationDto;

  brsLookupRequest: BrsLookUpRequest;
  businessLines$: Observable<BusinessLines[]>;
  businessNatures$: Observable<BusinessNatures[]>;
  region$: Observable<Region[]>;
  county$: Observable<County[]>;
  regionList: Region[];
  countyList: County[];
  town$: Observable<Town[]>;
  selectedBusinessLine = 0;
  selectedBusinessNature = 0;
  selectedRegion = 0;
  selectedCounty = 0;
  selectedTown = 0;
  validationCellphone = '';
  otpSent = false;
  phoneValidated = false;
  // @ts-ignore
  company: Company;
  // @ts-ignore
  user: User;
  submitted = false;



  constructor(
      private msService: MsService,
      private service: RegistrationPayloadService,
      private SpinnerService: NgxSpinnerService,
      private linesService: BusinessLinesService,
      private naturesService: BusinessNaturesService,
      private regionService: RegionService,
      private countyService: CountyService,
      private townService: TownService,
      private formBuilder: FormBuilder,
      private store$: Store<any>,

  ) {


    this.businessNatures$ = naturesService.entities$;
    this.businessLines$ = linesService.entities$;
    this.region$ = regionService.entities$;
    this.county$ = countyService.entities$;
    this.town$ = townService.entities$;

    regionService.getAll().subscribe();
    countyService.getAll().subscribe();
    // townService.getAll().subscribe();
    naturesService.getAll().subscribe();
    linesService.getAll().subscribe();

  }



  ngOnInit(): void {
    this.timer = interval(1000).pipe(takeUntil(this.ispause));

    this.timerObserver = {

      next: (_: number) => {
        if (this.time === 0) {
          // tslint:disable-next-line:no-unused-expression
          this.ispause.next;
        }
        this.time -= 1;
      },
    };


    this.countyService.getAll().subscribe((data: County[]) => {
      // this.countyList = data.sort((a, b) => a.county - b.county);
      this.countyList = data.sort((a, b) => a.county > b.county ? 1 : -1);
        },
    );

    this.regionService.getAll().subscribe((regionData: Region[]) => {
      this.regionList = regionData.sort((a, b) => a.region > b.region ? 1: -1);
    });

    this.stepZeroForm = this.formBuilder.group({
      registrationNumber: ['', Validators.required],
      directorIdNumber: ['', Validators.required],
    });

    // this.stepZeroForm = new FormGroup({
    //   registrationNumber: new FormControl('',[Validators.required]),
    //   directorIdNumber: new FormControl('',[Validators.required]),
    // });
    this.stepOneForm = new FormGroup({
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      emailAddress: new FormControl('', [Validators.required]),
      phoneNumber: new FormControl('', [Validators.required]),
      postalAddress: new FormControl('', [Validators.required]),
      physicalAddress: new FormControl('', [Validators.required]),
      idNumber: new FormControl(),
    });

    this.stepTwoForm = new FormGroup({
      // postalAddress: new FormControl(),
      complaintTitle: new FormControl('', [Validators.required]),
      complaintDescription: new FormControl('', [Validators.required]),
      complaintSampleDetails: new FormControl('', [Validators.required]),
      remedySought: new FormControl('', [Validators.required]),
      // complaintCategory: new FormControl('', [Validators.required]),
      // myProduct: new FormControl('', [Validators.required]),
      productBrand: new FormControl('', [Validators.required]),
      productName: new FormControl('',[Validators.required]),
    });

    this.stepThreeForm = new FormGroup({
      region: new FormControl(),
      county: new FormControl('', [Validators.required]),
      town: new FormControl('', [Validators.required]),
      marketCenter: new FormControl('', [Validators.required]),
      buildingName: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required]),
      nameContactPerson: new FormControl('', [Validators.required]),
      phoneNumber: new FormControl('', [Validators.required]),
      telephoneNumber: new FormControl(),
      businessAddress: new FormControl('', [Validators.required]),
    });

    this.stepFourForm = this.formBuilder.group({
          firstName: [],
          lastName: ['', Validators.required],
          userName: ['', Validators.required],
          email: ['', Validators.required],
          credentials: ['', Validators.required],
          confirmCredentials: ['', [Validators.required]],
        },
        {
          validators: ConfirmedValidator('credentials', 'confirmCredentials'),
        });

    this.stepFiveForm = this.formBuilder.group({
      cellphone: ['', Validators.required],
      otp: ['', Validators.required],
    });


  }


  get formStepZeroForm(): any {
    return this.stepZeroForm.controls;
  }

  get formStepOneForm(): any {
    return this.stepOneForm.controls;
  }

  get formStepTwoForm(): any {
    return this.stepTwoForm.controls;
  }

  get formStepThreeForm(): any {
    return this.stepThreeForm.controls;
  }

  get formStepFourForm(): any {
    return this.stepFourForm.controls;
  }

  get formStepFiveForm(): any {
    return this.stepFiveForm.controls;
  }

  // sortFnCounty = (a: County, b: County): number => {
  //   if (a.county < b.county) { return -1; }
  //   if (a.county === b.county) { return 0; }
  //   if (a.county > b.county) { return 1; }
  // }

  updateSelectedRegion() {
    this.selectedRegion = this.stepThreeForm?.get('region')?.value;
    this.regionService.getAll().subscribe((regionData : Region[]) =>{
      this.regionName = regionData?.find(x => x.id === this.selectedRegion).region;
    });
    console.log("Region selected is: " + this.selectedRegion);
  }

  updateSelectedCounty() {
    this.selectedCounty = this.stepThreeForm?.get('county')?.value;
    this.countyService.getAll().subscribe((data: County[]) => {
          this.countyName = data?.find(x => x.id === this.selectedCounty).county;
        },
    );
    console.log(`county set to ${this.selectedCounty}`);
    this.store$.dispatch(loadCountyId({payload: this.selectedCounty}));
    this.store$.select(selectCountyIdData).subscribe(
        (d) => {
          if (d) {
            console.log(`Select county inside is ${d}`);
            return this.townService.getAll();
          } else {
            return throwError('Invalid request, Company id is required');
          }
        },
    );

  }

  updateSelectedTown() {
    this.selectedTown = this.stepThreeForm?.get('town')?.value;
    // tslint:disable-next-line:no-shadowed-variable
    this.townService.getAll().subscribe((data: Town[]) => {
          this.townName = data?.find(x => x.id === this.selectedTown).town;
        },
    );
    console.log(`town set to ${this.selectedTown}`);
  }

  onClickRegisterCompany(valid: boolean) {
    if (valid) {
      // if (this.phoneValidated) {
      //   this.company = {...this.company, ...this.companySoFar};
      //   this.user = {...this.user, ...this.userSoFar};
      //
      //   this.store$.dispatch(loadRegistrations({
      //     payload: {company: this.company, user: this.user}
      //   }));
      //
      //   this.store$.pipe(select(selectRegistrationStateSucceeded)).subscribe((d) => {
      //     console.log(`status inside is ${d}`);
      //     if (d) {
      //       return this.store$.dispatch(Go({payload: '', link: 'login', redirectUrl: ''}));
      //     }
      //   });
      // } else {
      //   this.otpSent = false;
      //   this.stepFiveForm.get('otp')?.reset();
      //   this.store$.dispatch(loadResponsesFailure({
      //     error: {
      //       payload: 'Cellphone needs to be validated',
      //       status: 100,
      //       response: '05'
      //     }
      //   }));
      // }
    } else {
      this.store$.dispatch(loadResponsesFailure({
        error: {
          payload: 'Some required details are missing, kindly recheck',
          status: 100,
          response: '05',
        },
      }));

    }

  }


  resetPreviewDocs() {
    this.arrayOfUploadedFiles.splice(0);
  }

  reviewComplaint() {
    if (this.uploadedFiles){
      for( let i =0; i<this.uploadedFiles.length; i++){
        this.arrayOfUploadedFiles.push(this.uploadedFiles[i]);
      }
    }
    this.customerDetails = this.stepOneForm.value;
    this.complaintDetails = this.stepTwoForm.value;
    this.locationDetails = this.stepThreeForm.value;

    window.$('#complaintModal').modal('show');
  }

  async onSubmitComplaint() {
    this.submitted = true;
    this.msService.showSuccessWith2Message('By Clicking Yes', 'The information I have availed to the Kenya Bureau of Standards in' +
        ' support of this complaint is true and correct to the best of my ' +
        'knowledge and is made available on the understanding that the Kenya Bureau ' +
        'of Standards, its employees and agents take no responsibility for the accuracy,' +
        ' currency, reliability and correctness of this information and shall have no ' +
        'liability for any loss, damage, cost or expense incurred or arising by reason of ' +
        'any person using or relying on this information and whether caused by reason of any ' +
        'error, negligent act, omission or misrepresentation in this information or otherwise.' +
        ' I have consented to the collection and processing of my personal data by the Kenya ' +
        'Bureau of Standards for the purposes of investigation of this complaint and feedback ' +
        'on the findings of investigation. Without prejudice to my constitutional rights, ' +
        'I shall not, unless with lawful authority, disclose any information provided to me ' +
        'by the Kenya Bureau of Standards in relation to this complaint!',
        // tslint:disable-next-line:max-line-length
        'You can go back and click the \'PREV\' buttons to Update Details Before Saving', 'COMPLAINT SUBMITTED SUCCESSFUL', () => {
          this.saveDetailsFirst();
        });

  }

  saveDetailsFirst(): any {
    console.log('Button Clicked');
    if (this?.uploadedFiles?.length > 0) {
      this.SpinnerService.show();
      const file = this.uploadedFiles;
      const newComplaintDto = new NewComplaintDto();
      newComplaintDto.customerDetails = this.stepOneForm.value;
      newComplaintDto.complaintDetails = this.stepTwoForm.value;
      newComplaintDto.locationDetails = this.stepThreeForm.value;
      const formData = new FormData();
      formData.append('data', JSON.stringify(newComplaintDto));
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
      this.msService.saveComplaintFiles(formData).subscribe(
          (data2: MSComplaintSubmittedSuccessful) => {
            console.log(data2);
            this.savedDetails = data2;
            this.msService.showSuccess(data2.successMessage);
            this.SpinnerService.hide();
            return this.store$.dispatch(Go({payload: '', link: 'login', redirectUrl: ''}));
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN Error Occurred, Try Again Later');
          },
      );
    } else {
      console.log('Button Clicked Moved To Else STatment');
      this.SpinnerService.show();
      const newComplaintDto = new NewComplaintDto();
      newComplaintDto.customerDetails = this.stepOneForm.value;
      newComplaintDto.complaintDetails = this.stepTwoForm.value;
      newComplaintDto.locationDetails = this.stepThreeForm.value;
      this.msService.createNewComplaint(newComplaintDto).subscribe(
          (data2: MSComplaintSubmittedSuccessful) => {
            console.log(data2);
            this.savedDetails = data2;
            this.msService.showSuccess(data2.successMessage);
            this.SpinnerService.hide();
            return this.store$.dispatch(Go({payload: '', link: 'login', redirectUrl: ''}));
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN Error Occurred, Try Again Later');
          },
      );
    }

  }

  onClickValidateOtp() {
    this.phoneValidated = true;
    this.store$.dispatch(loadValidateTokenAndPhone({
      payload: {
        phone: this.stepFiveForm?.get('cellphone')?.value,
        token: this.stepFiveForm?.get('otp')?.value,
      },
    }));
    this.store$.pipe(select(selectValidateTokenAndPhoneValidated)).subscribe((d) => {
      console.log(`status inside is ${d}`);
      if (d) {
        this.otpSent = true;
        // this.stepFourForm?.get('otp')?.reset();
        this.phoneValidated = d;
        if (this.phoneValidated) {
          this.complaintSoFar = {...this.complaintSoFar, ...this.stepFiveForm.value};
          this.company = {...this.company, ...this.complaintCustomerSoFar};
          this.user = {...this.user, ...this.complaintSoFar};

          this.store$.dispatch(loadRegistrations({
            payload: {company: this.company, user: this.user},
          }));

          this.store$.pipe(select(selectRegistrationStateSucceeded)).subscribe((succeeded) => {
            console.log(`status inside is ${succeeded}`);
            if (succeeded) {
              return this.store$.dispatch(Go({payload: '', link: 'login', redirectUrl: ''}));
            }
          });
        } else {
          this.otpSent = false;
          this.stepFiveForm.get('otp')?.reset();
          this.store$.dispatch(loadResponsesFailure({
            error: {
              payload: 'Cellphone needs to be validated',
              status: 100,
              response: '05',
            },
          }));
        }

      } else {
        this.otpSent = false;
        this.phoneValidated = false;
        // this.stepFourForm?.get('otp')?.reset();
        return throwError('Could not validate token');

      }
    });

  }

  secondsToHms(d: number) {
    d = Number(d);
    // const m = Math.floor(d % 3600 / 60);
    const s = Math.floor(d % 3600 % 60);

    // const mDisplay = m > 0 ? m + (m == 1 ? ": " : " : ") : "00";
    // const sDisplay = s > 0 ? s + (s == 1 ? "" : "") : "00";
    // tslint:disable-next-line:triple-equals
    return s > 0 ? s + (s == 1 ? '' : '') : '00';
  }

  onClickSendOtp() {
    this.otpSent = true;
    this.time = 59;
    this.timer.subscribe(this.timerObserver);
    this.validationCellphone = this.stepFiveForm?.get('cellphone')?.value;

    this.stepFiveForm?.get('otp')?.reset();

    if (
        this.validationCellphone === '' ||
        this.validationCellphone === null
    ) {
      this.store$.dispatch(loadResponsesFailure({
        error: {
          payload: 'Enter a valid cellphone number',
          status: 100,
          response: '05',
        },
      }));

    } else {
      this.store$.dispatch(loadSendTokenToPhone({
        payload: {
          phone: this.validationCellphone,
        },
      }));

      this.store$.pipe(select(selectTokenSentStateOtpSent)).subscribe((d) => {
        console.log(`value of inside is ${d}`);
        if (d) {
          return this.otpSent = d;
        } else {
          return throwError('Unable to send token');
        }
      });
    }


  }

  onClickPrevious() {
    if (this.step > 1) {
      this.step = this.step - 1;
    } else {
      this.step = 1;
    }
  }

  onClickNext(valid: boolean) {
    if (valid) {
      switch (this.step) {
        case 1:
          this.complaintCustomerSoFar = {...this.complaintCustomerSoFar, ...this.stepOneForm?.value};
          break;
        case 2:
          this.complaintCustomerSoFar = {...this.complaintCustomerSoFar, ...this.stepOneForm?.value};
          break;
        case 3:
          this.complaintSoFar = {...this.complaintSoFar, ...this.stepTwoForm?.value};
          break;
        case 4:
          this.complaintLocationSoFar = {...this.complaintLocationSoFar, ...this.stepThreeForm?.value};
          break;
        // case 4:
        //   this.userSoFar = {...this.userSoFar, ...this.stepFourForm?.value};
        //   break;
        // case 5:
        //   this.userSoFar = this.stepFiveForm?.value;
        //   break;
      }
      this.step += 1;
    }

  }



  getFileUrl(fileUploaded: File){
    let createdUrl = URL.createObjectURL(fileUploaded);
    return createdUrl;
  }
}

