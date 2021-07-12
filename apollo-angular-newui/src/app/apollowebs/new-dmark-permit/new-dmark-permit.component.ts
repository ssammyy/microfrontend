import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {loadAuths, LoginCredentials} from '../../core/store';
import {Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {QaService} from '../../core/store/data/qa/qa.service';
import {PermitEntityDetails, PlantDetailsDto, SectionDto} from '../../core/store/data/qa/qa.model';
import {UserRegister} from '../../../../../apollo-webs/src/app/shared/models/user';
import swal from 'sweetalert2';

@Component({
    selector: 'app-new-dmark-permit',
    templateUrl: './new-dmark-permit.component.html',
    styleUrls: ['./new-dmark-permit.component.css']
})
export class NewDmarkPermitComponent implements OnInit {
    sta1Form: FormGroup;
    sta3FormA: FormGroup;
    sta3FormB: FormGroup;
    sta3FormC: FormGroup;
    sta3FormD: FormGroup;
    public credential: LoginCredentials;
    returnUrl: string;
    sections: SectionDto[];
    plants: PlantDetailsDto[];
    permitEntityDetails: PermitEntityDetails;
    stepSoFar: | undefined;
    step = 1;
    currBtn: string = 'A';


    constructor(private store$: Store<any>,
                private router: Router,
                private route: ActivatedRoute,
                private qaService: QaService,
                private formBuilder: FormBuilder,
    ) {
    }

    ngOnInit(): void {

        this.sta1Form = this.formBuilder.group({
            commodityDescription: ['', Validators.required],
            applicantName: ['', Validators.required],
            sectionId: ['', Validators.required],
            permitForeignStatus: ['', Validators.required],
            attachedPlant: ['', Validators.required],
            tradeMark: ['', Validators.required]

        });

        this.sta3FormA = this.formBuilder.group({
            produceOrdersOrStock: ['', Validators.required],
            issueWorkOrderOrEquivalent: ['', Validators.required],
            identifyBatchAsSeparate: ['', Validators.required],
            productsContainersCarryWorksOrder: ['', Validators.required],
            isolatedCaseDoubtfulQuality: ['', Validators.required]

        });

        this.sta3FormB = this.formBuilder.group({
            headQaQualificationsTraining: ['', Validators.required],
            reportingTo: ['', Validators.required],
            separateQcid: ['', Validators.required],
            testsRelevantStandard: ['', Validators.required],
            spoComingMaterials: ['', Validators.required],
            spoProcessOperations: ['', Validators.required],
            spoFinalProducts: ['', Validators.required],
            monitoredQcs: ['', Validators.required],
            qauditChecksCarried: ['', Validators.required],
            informationQcso: ['', Validators.required],

        });

        this.sta3FormC = this.formBuilder.group({
            mainMaterialsPurchasedSpecification: ['', Validators.required],
            adoptedReceiptMaterials: ['', Validators.required],
            storageFacilitiesExist: ['', Validators.required],

        });

        this.sta3FormD = this.formBuilder.group({
            stepsManufacture: ['', Validators.required],
            maintenanceSystem: ['', Validators.required],
            qcsSupplement: ['', Validators.required],
            qmInstructions: ['', Validators.required],
            testEquipmentUsed: ['', Validators.required],
            indicateExternalArrangement: ['', Validators.required],
            levelDefectivesFound: ['', Validators.required],
            levelClaimsComplaints: ['', Validators.required],
            independentTests: ['', Validators.required],
            indicateStageManufacture: ['', Validators.required],

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

        this.returnUrl = this.route.snapshot.queryParams[`returnUrl`] || `/dmark`;
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
                    this.stepSoFar = {...this.sta3FormA?.value};
                    break;
                case 3:
                    this.stepSoFar = {...this.sta3FormB?.value};
                    break;
                case 4:
                    this.stepSoFar = {...this.sta3FormC?.value};
                    break;
                case 5:
                    this.stepSoFar = {...this.sta3FormD?.value};
                    break;
                case 6:
                    this.stepSoFar = {...this.sta3FormD?.value};
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
    selectStepFiveClass(step: number): string {
        if (step === 1) {
            return 'active';
        }if (step === 2) {
            return 'activated';
        }if (step === 3) {
            return 'activated';
        }if (step === 4) {
            return 'activated';
        }if (step === 5) {
            return 'activated';
        }if (step === 6) {
            return 'activated';
        } else {
            return '';
        }
    }
    selectStepSixClass(step: number): string {
        if (step === 1) {
            return 'active';
        }if (step === 2) {
            return 'activated';
        }if (step === 3) {
            return 'activated';
        }if (step === 4) {
            return 'activated';
        }if (step === 5) {
            return 'activated';
        }if (step === 6) {
            return 'activated';
        } else {
            return '';
        }
    }

    get formSta1Form(): any {
        return this.sta1Form.controls;
    }

    get formSta3FormA(): any {
        return this.sta3FormA.controls;
    }

    get formSta3FormB(): any {
        return this.sta3FormB.controls;
    }

    get formSta3FormC(): any {
        return this.sta3FormC.controls;
    }

    get formSta3FormD(): any {
        return this.sta3FormD.controls;
    }

    onClickSaveSTA1(valid: boolean) {
        if (valid) {
            if (this.permitEntityDetails == null) {
                this.qaService.savePermitSTA1('1', this.sta1Form.value).subscribe(
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
                        this.step += 1;
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

    onClickSaveSTA3A(valid: boolean) {
        if (valid) {
            console.log(this.permitEntityDetails.id.toString());
            this.qaService.savePermitSTA3(this.permitEntityDetails.id.toString(), this.sta3FormA.value).subscribe(
                (data: any) => {
                    console.log(data);
                    this.step += 1;
                    swal.fire({
                        title: "STA3 Form saved!",
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

    onClickUpdateSTA3B(valid: boolean) {
        if (valid) {
            this.qaService.updatePermitSTA3(this.permitEntityDetails.id.toString(), this.sta3FormB.value).subscribe(
                (data: any) => {
                    console.log(data);
                    this.step += 1;
                    swal.fire({
                        title: "STA3 Form updated!",
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

    onClickUpdateSTA3C(valid: boolean) {
        if (valid) {
            this.qaService.updatePermitSTA3(this.permitEntityDetails.id.toString(), this.sta3FormC.value).subscribe(
                (data: any) => {
                    console.log(data);
                    this.step += 1;
                    swal.fire({
                        title: "STA3 Form updated!",
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

    onClickUpdateSTA3D(valid: boolean) {
        if (valid) {
            this.qaService.updatePermitSTA3(this.permitEntityDetails.id.toString(), this.sta3FormD.value).subscribe(
                (data: any) => {
                    console.log(data);
                    this.step += 1;
                    swal.fire({
                        title: "STA3 Form updated!",
                        buttonsStyling: false,
                        customClass:{
                            confirmButton: "btn btn-success form-wizard-next-btn ",
                        },
                        icon: "success"
                    });
                    this.router.navigate(['/dmark'], {fragment: this.permitEntityDetails.id.toString()});
                },
            );
        }
    }
}
