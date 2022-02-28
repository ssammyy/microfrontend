import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {faArrowLeft} from '@fortawesome/free-solid-svg-icons/faArrowLeft';
import {ActivatedRoute, Router} from '@angular/router';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MarketSurveillanceService} from '../../../shared/services/market-surveillance.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {NotificationService} from '../../../shared/services/notification.service';
import {Complaints} from '../../../shared/models/complaints';
import {DivisionDetails} from '../../../shared/models/master-data-details';
import {AlertService} from '../../../shared/services/alert.service';
import {AccountService} from '../../../shared/services/account.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-complaint-details',
  templateUrl: './complaint-details.component.html',
  styleUrls: ['./complaint-details.component.css']
})
export class ComplaintDetailsComponent implements OnInit {

  @ViewChild('editModal') editModal !: TemplateRef<any>;
  currDiv!: string;
  currDivLabel!: string;

  arrowLeftIcon = faArrowLeft;
  public selectedComplaint: any;
  selectedValue = '0';
  checkBoxValue = false;
  public selectForm!: FormGroup;
  public acceptComplaintForm!: FormGroup;
  public rejectComplaintForm!: FormGroup;
  public adviceComplaintForm!: FormGroup;
  public assignIOComplaintForm!: FormGroup;
  public selectedOption!: string;
  public refNumber!: string;
  public personsData: any[] = [];
  public persons!: FormArray;
  public complaint!: Complaints;


  divisionOptions: DivisionDetails[] = [];

  rejectOptions = [
    {
      id: '1',
      option: 'Falls within other OGA'
    },
    {
      id: '2',
      option: 'Does not Fall within other OGA  '
    }
  ];


  get formKEBSMandateIn(): any {
    return this.acceptComplaintForm.controls;
  }

  get formKEBSNotMandateIn(): any {
    return this.rejectComplaintForm.controls;
  }

  constructor(private router: Router,
              private marketSurveillanceService: MarketSurveillanceService,
              private route: ActivatedRoute,
              private modalService: NgbModal,
              private spinner: NgxSpinnerService,
              public accountService: AccountService,
              private alertService: AlertService,
              private notificationService: NotificationService,
              private formBuilder: FormBuilder
  ) {
  }

  get formKEBSNotMandateButWithinOGA(): any {
    return this.adviceComplaintForm.controls;
  }

  get formKEBSAssignIO(): any {
    return this.assignIOComplaintForm.controls;
  }

  ngOnInit(): void {
    this.getSelectedComplaint();

    this.acceptComplaintForm = this.formBuilder.group({
      division: ['', Validators.required],
      approvedRemarks: ['', Validators.required],
    });

    this.rejectComplaintForm = this.formBuilder.group({
      // rejectType: ['', Validators.required],
      rejectedRemarks: ['', Validators.required],
    });

    this.adviceComplaintForm = this.formBuilder.group({
      // mandateForOga: ['', Validators.required],
      rejectedRemarks: ['', Validators.required],
      advisedWhereToRemarks: ['', Validators.required],
    });

    this.assignIOComplaintForm = this.formBuilder.group({
      assignedIo: ['', Validators.required],
      assignedRemarks: ['', Validators.required],
      // advisedWhereToRemarks: ['', Validators.required],
    });
  }

  public onChangedCheckBox(value: boolean): void {
    console.log(value);
    this.checkBoxValue = value;
  }

  public getSelectedComplaint(): void {
    this.route.fragment.subscribe(params => {
      this.refNumber = params;
      console.log(this.refNumber);
      this.marketSurveillanceService.loadMSComplaintDetail(this.refNumber).subscribe(
        (data: Complaints) => {
          this.complaint = data;
          console.log(data);

        },
        // tslint:disable-next-line:max-line-length
        (error: { error: { message: any; }; }) => {
          this.notificationService.showError(error.error.message, 'Access Denied');
          this.spinner.hide();
        }
      );
    });
  }


  openModal(divVal: string): void {
    const arrHead = ['rejectComplaint', 'rejectOGAComplaint', 'approveComplaint', 'assignOfficer'];
    const arrHeadSave = ['Not Within KEBS Mandate or OGA', 'Not Within KEBS Mandate but Within OGA', 'KEBS Mandate', 'Assign Officer'];

    for (let h = 0; h < arrHead.length; h++) {
      if (divVal === arrHead[h]) {
        this.currDivLabel = arrHeadSave[h];
      }
    }

    this.currDiv = divVal;
    this.modalService.open(this.editModal);
  }

  onUpdateReturnToList(): void {
    this.router.navigate(['/complaints-page']);
  }

  onSubmitKEBSMandate(refNumber: string): void {
    this.marketSurveillanceService.updateComplaintKEBSMandate(this.acceptComplaintForm.value, refNumber).subscribe(
      (data: Complaints) => {
        console.log(data);
        this.onUpdateReturnToList();
      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );
  }

  onRejectKEBSMandate(refNumber: string): void {
    this.marketSurveillanceService.updateComplaintKEBSNotWithInMandate(this.rejectComplaintForm.value, refNumber).subscribe(
      (data: Complaints) => {
        console.log(data);
        this.onUpdateReturnToList();
      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );
  }

  onAdviceKEBSMandate(refNumber: string): void {
    this.marketSurveillanceService.updateComplaintKEBSNotWithInMandateButOGA(this.adviceComplaintForm.value, refNumber).subscribe(
      (data: Complaints) => {
        console.log(data);
        this.onUpdateReturnToList();
      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.alertService.error(error.error.message, 'Access Denied');
        // this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );
  }

  onAssignOfficer(refNumber: string): void {
    this.marketSurveillanceService.assignComplaintToIO(this.assignIOComplaintForm.value, refNumber).subscribe(
      (data: Complaints) => {
        console.log(data);
        this.onUpdateReturnToList();
      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.alertService.error(error.error.message, 'Access Denied');
        // this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );
  }

}
