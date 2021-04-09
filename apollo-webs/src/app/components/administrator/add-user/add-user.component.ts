import {Component, OnInit} from '@angular/core';
import {faArrowLeft} from '@fortawesome/free-solid-svg-icons/faArrowLeft';
import {faTrash} from '@fortawesome/free-solid-svg-icons';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
  Counties,
  Department,
  DesignationEntityDto,
  DirectoratesEntityDto,
  DivisionDetails,
  RegionsEntityDto,
  SectionsEntityDto,
  SubSectionsL1EntityDto,
  SubSectionsL2EntityDto,
  TitlesEntityDto,
  Towns
} from '../../../shared/models/master-data-details';
import {MasterDataService} from '../../../shared/services/master-data.service';
import {AlertService} from '../../../shared/services/alert.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {NotificationService} from '../../../shared/services/notification.service';
import {UserRegister} from '../../../shared/models/user';
import {AdministratorService} from '../../../shared/services/administrator.service';
import {Router} from '@angular/router';


@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {

  arrowLeftIcon = faArrowLeft;
  deleteIcon = faTrash;
  loading = false;
  submitted = false;
  returnUrl!: string;

  public addUserFormGroup!: FormGroup;

  public directoratesEntityDto!: DirectoratesEntityDto[];
  public designationEntityDto!: DesignationEntityDto[];
  public regionsEntityDto!: RegionsEntityDto[];
  public countiesDto!: Counties[];
  public townsDto!: Towns[];
  public departmentDto!: Department[];
  public divisionDto!: DivisionDetails[];
  public sectionsEntityDto!: SectionsEntityDto[];
  public subSectionsL1EntityDto!: SubSectionsL1EntityDto[];
  public subSectionsL2EntityDto!: SubSectionsL2EntityDto[];


  titles!: TitlesEntityDto[];
  directorates: DirectoratesEntityDto[] = [];
  designations: DesignationEntityDto[] = [];
  department: Department[] = [];
  division: DivisionDetails[] = [];
  sections: SectionsEntityDto[] = [];
  l1SubSubSections: SubSectionsL1EntityDto[] = [];
  l2SubSubSections: SubSectionsL2EntityDto[] = [];
  counties: Counties[] = [];
  towns: Towns[] = [];


  constructor(private formBuilder: FormBuilder,
              private notificationService: NotificationService,
              private spinner: NgxSpinnerService,
              private router: Router,
              private alertService: AlertService,
              private masterDataService: MasterDataService,
              private administratorService: AdministratorService
  ) {
  }

  get formAddUser(): any {
    return this.addUserFormGroup.controls;
  }

  ngOnInit(): void {
    this.getALlRecommendedDetails();

    this.addUserFormGroup = this.formBuilder.group({
      title: ['', Validators.required],
      email: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      directorate: ['', Validators.required],
      designation: ['', Validators.required],
      department: ['', Validators.required],
      division: ['', Validators.required],
      section: ['', null],
      l1SubSubSection: ['', null],
      l2SubSubSection: ['', null],
      region: ['', Validators.required],
      county: ['', Validators.required],
      town: ['', Validators.required],
    });
  }

  onSelectDirectorate(directorateID: number): any {
    this.designations = this.designationEntityDto.filter((item) => {
      return item.directorateId === Number(directorateID);
    });
    this.department = this.departmentDto.filter((item1) => {
      return item1.directorateId === Number(directorateID);
    });
  }

  onSelectDepartment(departmentID: number): any {
    this.division = this.divisionDto.filter((item) => {
      return item.departmentId === Number(departmentID);
    });
  }

  onSelectDivision(divisionID: number): any {
    this.sections = this.sectionsEntityDto.filter((item) => {
      return item.divisionId === Number(divisionID);
    });
  }

  onSelectSection(sectionID: number): any {
    this.l1SubSubSections = this.subSectionsL1EntityDto.filter((item) => {
      return item.sectionId === Number(sectionID);
    });
  }

  onSelectL1SubSubSection(l1SubSubSectionID: number): any {
    this.l2SubSubSections = this.subSectionsL2EntityDto.filter((item) => {
      return item.subSectionL1Id === Number(l1SubSubSectionID);
    });
  }

  onSelectRegion(regionID: number): any {
    this.counties = this.countiesDto.filter((item) => {
      return item.regionId === Number(regionID);
    });
  }

  onSelectCounty(countyID: number): any {
    this.towns = this.townsDto.filter((item) => {
      return item.countyId === Number(countyID);
    });
  }

  public getALlRecommendedDetails(): void {
    this.masterDataService.loadTitlesSystemAdmin().subscribe(
      (data: any) => {
        this.titles = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadDirectorateSystemAdmin().subscribe(
      (data: any) => {
        this.directoratesEntityDto = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadDesignationsSystemAdmin().subscribe(
      (data: any) => {
        this.designationEntityDto = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadDepartmentsSystemAdmin().subscribe(
      (data: any) => {
        this.departmentDto = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadDivisionsSystemAdmin().subscribe(
      (data: any) => {
        this.divisionDto = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadSectionSystemAdmin().subscribe(
      (data: any) => {
        this.sectionsEntityDto = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadL1SubSubSectionSystemAdmin().subscribe(
      (data: any) => {
        this.subSectionsL1EntityDto = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadL2SubSubSectionSystemAdmin().subscribe(
      (data: any) => {
        this.subSectionsL2EntityDto = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadRegionsSystemAdmin().subscribe(
      (data: any) => {
        this.regionsEntityDto = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadCounties().subscribe(
      (data: any) => {
        this.countiesDto = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadTowns().subscribe(
      (data: any) => {
        this.townsDto = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );
  }


  onSubmit(): any {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.addUserFormGroup.invalid) {
      return;
    }
    this.loading = true;
    this.administratorService.registerEmployee(this.addUserFormGroup.value).subscribe(
      (data: UserRegister) => {
        console.log(data);
        this.router.navigate(['/users-list']);
      },
      (error: { error: { message: any; }; }) => {
        this.alertService.error(error.error.message, 'Access Denied');
        // this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      });
  }
}
