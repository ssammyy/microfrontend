import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {
  Back,
  Branches,
  BranchesService,
  Company,
  Go,
  loadBranchId,
  loadCompanyId,
  selectCompanyData,
  selectCompanyIdData, selectUserInfo
} from 'src/app/core/store';
import {Store} from '@ngrx/store';
import {Router} from "@angular/router";
import {QaService} from '../../../core/store/data/qa/qa.service';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
  selector: 'app-branch',
  templateUrl: './branch.list.html',
  styleUrls: ['../company.component.css'
  ]
})
export class BranchList implements OnInit {

  branches$: Observable<Branches[]>;
  filterName = '';
  stepTwoForm!: FormGroup;
  uploadInspection!: FormGroup;
  stepThreeForm!: FormGroup;
  // @ts-ignore
  branch: Branches;
  company$: Company | undefined;
  submitted = false;
  selectedCompany = 0;
  roles: string[];
  blob: Blob;

  loading = false;
  loadingText: string;

  selectedBranch: Branches;
  uploadedFilesOnly: FileList;
  currDivLabel!: string;
  currDiv!: string;

  constructor(
      private service: BranchesService,
      private qaService: QaService,
      private SpinnerService: NgxSpinnerService,
      private store$: Store<any>,
      private formBuilder: FormBuilder,
      private router: Router
  ) {
    this.branches$ = service.entities$;
    service.getAll().subscribe();
  }

  ngOnInit(): void {
    this.store$.select(selectCompanyIdData).subscribe((d) => {
      return this.selectedCompany = d;
    });

    this.uploadInspection = this.formBuilder.group({
      userPaidDate: ['', Validators.required],
    });

    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.roles = u.roles;
    });

    this.store$.select(selectCompanyData).subscribe((d) => {
      return this.company$ = d;
    });

  }


  onClickPlantDetails(record: Company) {
    this.store$.dispatch(loadCompanyId({payload: record.id, company: record}));
    this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'company/branches'}));
  }


  editRecord(record: Branches) {

    this.store$.dispatch(loadBranchId({payload: record.id, branch: record}));
    this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'companies/branch'}));

  }

  get formStepTwoForm(): any {
    return this.stepTwoForm.controls;
  }

  get formStepThreeForm(): any {
    return this.stepThreeForm.controls;
  }

  onClickUsers(record: Branches) {
    this.store$.dispatch(loadCompanyId({payload: record.companyProfileId, company: this?.company$}));
    this.store$.dispatch(loadBranchId({payload: record.id, branch: record}));
    this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'companies/branches/users'}));

  }

  addBranches() {
    // this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'dashboard/companies/branch'}));

    this.router.navigate(['branches/add_branch']);
  }

  public goBack(): void {
    this.store$.dispatch(Back());
  }

  viewRecord(record: Branches) {
    this.store$.dispatch(loadBranchId({payload: record.id, branch: record}));
    this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'companies/view/branch'}));
  }

  onClickGenerateInspectionFee(record: Branches) {
      this.qaService.showSuccessWith2Message('Are you sure your want to Generate inspection invoice?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          `You can click \'Generate Inspection Invoice\' button to updated the Details before saving`, 'PDF SAVED SUCCESSFUL', () => {
            this.generateInspectionFee(record);
          });
  }

  uploadInspectionReport(record: Branches) {
    this.currDivLabel = `BRANCH INSPECTION INVOICE DETAILS : ${record.branchName}`;
    this.currDiv = 'uploadInspectionReport';
    this.selectedBranch = record;
    window.$('#myModalUpload').modal('show');
  }

  onClickUploadInspectionFee(record: Branches) {
    if (this.uploadedFilesOnly.length > 0) {
    this.qaService.showSuccessWith2Message('Are you sure your want to Upload the selected inspection invoice?', 'You won\'t be able to revert back after submission!',
        // tslint:disable-next-line:max-line-length
        `You can click \'Upload Inspection Invoice\' button to updated the Details before saving`, 'PDF SAVED SUCCESSFUL', () => {
          this.saveFilesResults(record);
        });
    } else {
      this.qaService.showError('NO FILE SELECTED FOR UPLOADED');
    }
  }

  saveFilesResults(record: Branches) {
    if (this.uploadedFilesOnly.length > 0) {
      this.SpinnerService.show();
      const file = this.uploadedFilesOnly;
      const formData = new FormData();
      formData.append('branchID', String(record.id));
      formData.append('userPaidDate', String(this.uploadInspection.get('userPaidDate').value));
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
      this.qaService.saveUploadFile(formData).subscribe(
          (data: any) => {
            console.log(data);
            // this.loadStandards();
            this.SpinnerService.hide();
            this.qaService.showSuccess('FILE UPLOADED SAVED SUCCESSFULLY', () => {
              this.onClickPlantDetails(this.company$);
            });
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.qaService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  viewUploadDetailsPdfFile(ssfID: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.qaService.loadInspectionFeesUploadDetailsPDF(String(ssfID)).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});

          // tslint:disable-next-line:prefer-const
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = `#-${fileName}`;
          link.click();
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
        },
    );
  }



  generateInspectionFee(record: Branches) {

      this.qaService.generateInspectionFees(record.id).subscribe(
          (data: any) => {
            this.SpinnerService.hide();
            this.qaService.showSuccess('Inspection Fee Invoice Generated Successful', () => {
              this.onClickPlantDetails(this.company$);
            });
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.qaService.showError('AN ERROR OCCURRED');
          },
      );

  }
}
