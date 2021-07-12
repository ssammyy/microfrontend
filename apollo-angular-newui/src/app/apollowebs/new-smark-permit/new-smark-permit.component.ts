import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Company, loadAuths, LoginCredentials} from "../../core/store";
import {Store} from "@ngrx/store";
import {ActivatedRoute, Router} from "@angular/router";
import {QaService} from "../../core/store/data/qa/qa.service";
import {PermitEntityDetails, PlantDetailsDto, SectionDto} from "../../core/store/data/qa/qa.model";
import swal from "sweetalert2";

@Component({
  selector: 'app-new-smark-permit',
  templateUrl: './new-smark-permit.component.html',
  styleUrls: ['./new-smark-permit.component.css']
})
export class NewSmarkPermitComponent implements OnInit {
  sta1Form: FormGroup;
  sta10Form: FormGroup;
  sections: SectionDto[];
  plants: PlantDetailsDto[];
  permitEntityDetails: PermitEntityDetails;
  currBtn: string = 'A';
    stepSoFar: | undefined;
    step = 1;

  constructor(private store$: Store<any>,
              private router: Router,
              private qaService: QaService,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
        this.sta1Form = this.formBuilder.group({
          commodityDescription: ['', Validators.required],
          applicantName: ['', Validators.required],
          sectionId: ['', Validators.required],
          permitForeignStatus: [],
          attachedPlant: ['', Validators.required],
          tradeMark: ['', Validators.required]

        });
      this.sta10Form = this.formBuilder.group({
          produceOrdersOrStock: ['', Validators.required],
          issueWorkOrderOrEquivalent: ['', Validators.required],
          identifyBatchAsSeparate: ['', Validators.required],
          productsContainersCarryWorksOrder: ['', Validators.required],
          isolatedCaseDoubtfulQuality: ['', Validators.required]

      });


    this.qaService.loadSectionList().subscribe(
        (data: any) => {
          this.sections = data;
          console.log(data);
        }
    );

    this.qaService.loadPlantList().subscribe(
        (data: any) => {
          this.plants = data;
          console.log(data);
        }
    );

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
                    this.stepSoFar = {...this.sta1Form?.value};
                    break;
                case 2:
                    this.stepSoFar = {...this.sta1Form?.value};
                    break;
                case 3:
                    this.stepSoFar = {...this.sta1Form?.value};
                    break;
                case 4:
                    this.stepSoFar = {...this.sta1Form?.value};
                    break;
            }
            this.step += 1;
            console.log(`Clicked and step = ${this.step}`);
        }
    }

    selectStepOneClass(step: number): string {
        if (step === 1) {
            return 'active';
        } else {
            return '';
        }
    }
    selectStepTwoClass(step: number): string {
        console.log(`${step}`);
        if (step === 1) {
            return 'active';
        }if (step === 2) {
            return 'activated';
        } else {
            return '';
        }
    }
    selectStepThreeClass(step: number): string {
        if (step === 1) {
            return 'active';
        }if (step === 2) {
            return 'activated';
        }if (step === 3) {
            return 'activated';
        } else {
            return '';
        }
    }

    selectStepFourClass(step: number): string {
        if (step === 1) {
            return 'active';
        }if (step === 2) {
            return 'activated';
        }if (step === 3) {
            return 'activated';
        }if (step === 4) {
            return 'activated';
        } else {
            return '';
        }
    }

  get formSta1Form(): any {
    return this.sta1Form.controls;
  }

  onClickSaveSTA1(valid: boolean) {
    if (valid) {
      if (this.permitEntityDetails == null) {
        this.qaService.savePermitSTA1('2', this.sta1Form.value).subscribe(
            (data: PermitEntityDetails) => {
              this.permitEntityDetails = data;
              console.log(data);
                this.step += 1;
              this.currBtn = 'B';
              swal.fire({
                title: "STA1 Form saved!",
                buttonsStyling: false,
                customClass:{
                  confirmButton: "btn btn-success form-wizard-next-btn ",
                },
                icon: "success"
              });
// this.router.navigate(['/users-list']);
            },
        );
      } else {
        this.qaService.updatePermitSTA1(String(this.permitEntityDetails.id), this.sta1Form.value).subscribe(
            (data: PermitEntityDetails) => {
              this.permitEntityDetails = data;
              console.log(data);
              swal.fire({
                title: "STA1 Form updated!",
                buttonsStyling: false,
                customClass:{
                  confirmButton: "btn btn-success form-wizard-next-btn ",
                },
                icon: "success"
              });
// this.router.navigate(['/users-list']);
            },
        );
      }
    }
  }


}
