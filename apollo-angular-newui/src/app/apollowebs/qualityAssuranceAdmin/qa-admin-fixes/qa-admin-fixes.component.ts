import {Component, OnInit} from '@angular/core';
import swal from "sweetalert2";
import {MasterService} from "../../../core/store/data/master/master.service";
import {TitlesService} from "../../../core/store/data/title";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AddMssingStandardDto} from "../../../core/store/data/master/master.model";

@Component({
  selector: 'app-qa-admin-fixes',
  templateUrl: './qa-admin-fixes.component.html',
  styleUrls: ['./qa-admin-fixes.component.css']
})
export class QaAdminFixesComponent implements OnInit {
  standard : AddMssingStandardDto;
  formAddStandard: FormGroup = new FormGroup({});
  formTieFmark : FormGroup = new FormGroup({});


  data = [
    {
      id: 1,
      errorDescription: 'Missing Standard in KIMS',
      fixDescription: 'inserting or updating the standard',
      entities: 'CFG_SAMPLE_STANDARDS'
    },
    // Add more objects as needed
  ];
  checkInspectionReportsDuplicates(){
    this.masterService.removeInspectionReportsDuplicates().subscribe(
        (response: any) => {
          if (response.status === 200) {
            this.handleSuccessResponse("Duplicated records removed successfully")
          }
          else{
            this.handleInfoResponse("No duplicates found ")
          }
        },
        (error: any) => {
          const errorMessage = 'An error occurred. Please try again.';
          this.handleErrorResponse(errorMessage);
        }
    );

  }

  handleSuccessResponse(message: string) {
    swal.fire({
      title: 'Success',
      text: message,
      buttonsStyling: false,
      customClass: {
        confirmButton: 'btn btn-success form-wizard-next-btn',
      },
      icon: 'success'
    });
    this.router.navigate(['/'], { fragment: String(this.standard.standardNumber) });
  }

  handleErrorResponse(errorMessage: string) {
    swal.fire({
      title: 'Error',
      text: errorMessage,
      buttonsStyling: false,
      customClass: {
        confirmButton: 'btn btn-danger',
      },
      icon: 'error'
    });
  }
  handleInfoResponse(message: string) {
    swal.fire({
      title: 'Oops!',
      text: message,
      buttonsStyling: false,
      customClass: {
        confirmButton: 'btn btn-danger',
      },
      icon: 'info'
    });
  }

  onClickSubmit() {
    this.masterService.addMissingStandard(this.formAddStandard.value).subscribe(
        (data: any) => {
          this.standard = data;
          swal.fire({
            title: 'Standard added Successfully',
            buttonsStyling: false,
            customClass: {
              confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'success'
          });
          this.router.navigate(['/'], {fragment: String(this.standard.standardNumber)});
        });
  }
  get formAddStandards(): any {
    return this.formAddStandard.controls;
  }
  get formTieFmarks(): any {
    return this.formAddStandard.controls;
  }



  public onOpenModal(): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-target', '#assignPrivilege');


    // @ts-ignore
    container.appendChild(button);
    button.click();
  }




  constructor(private masterService: MasterService,
              private titleService: TitlesService,
              private router: Router,
              private route: ActivatedRoute,
              private formBuilder: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.formAddStandard = this.formBuilder.group({
      standardTitle: ['', Validators.required],
      standardNumber: ['', Validators.required],
      subCategoryId: ['', Validators.required],
      numberOfPages: ['', Validators.required],
    });
  }


}
