import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {loadAuths, LoginCredentials} from '../../core/store';
import {Store} from '@ngrx/store';
import {ActivatedRoute} from '@angular/router';
import {QaService} from '../../core/store/data/qa/qa.service';
import {PermitEntityDetails, PlantDetailsDto, SectionDto} from '../../core/store/data/qa/qa.model';
import {UserRegister} from '../../../../../apollo-webs/src/app/shared/models/user';

@Component({
    selector: 'app-new-dmark-permit',
    templateUrl: './new-dmark-permit.component.html',
    styleUrls: ['./new-dmark-permit.component.css']
})
export class NewDmarkPermitComponent implements OnInit {
    sta1Form: FormGroup;
    sta3FormA: FormGroup;
    public credential: LoginCredentials;
    returnUrl: string;
    sections: SectionDto[];
    plants: PlantDetailsDto[];
    permitEntityDetails: PermitEntityDetails;


    constructor(private store$: Store<any>,
                private route: ActivatedRoute,
                private qaService: QaService,
                private formBuilder: FormBuilder,
    ) {
    }

    ngOnInit(): void {
        // this.sta1Form = new FormGroup(
        //     {
        //         commodityDescription: new FormControl('', [Validators.required]),
        //         tradeMark: new FormControl('', [Validators.required]),
        //         applicantName: new FormControl('', [Validators.required]),
        //         sectionId: new FormControl('', [Validators.required]),
        //         permitForeignStatus: new FormControl('', [Validators.required]),
        //         attachedPlant: new FormControl('', [Validators.required]),
        //     }
        // );
        this.sta1Form = this.formBuilder.group({
            commodityDescription:['', Validators.required],
            applicantName:['', Validators.required],
            sectionId:['', Validators.required],
            permitForeignStatus: ['', Validators.required],
            attachedPlant: ['', Validators.required],
            tradeMark: ['', Validators.required]

        });

        this.sta3FormA = this.formBuilder.group({
            produceOrdersOrStock:['', Validators.required],
            issueWorkOrderOrEquivalent:['', Validators.required],
            identifyBatchAsSeparate:['', Validators.required],
            productsContainersCarryWorksOrder: ['', Validators.required],
            isolatedCaseDoubtfulQuality: ['', Validators.required],
            headQaQualificationsTraining: ['', Validators.required],
            reportingTo:['', Validators.required],
            separateQcid:['', Validators.required],
            testsRelevantStandard:['', Validators.required],
            spoComingMaterials: ['', Validators.required],
            spoProcessOperations: ['', Validators.required],
            spoFinalProducts: ['', Validators.required],
            monitoredQcs: ['', Validators.required],
            qauditChecksCarried:['', Validators.required],
            informationQcso:['', Validators.required],
            mainMaterialsPurchasedSpecification:['', Validators.required],
            adoptedReceiptMaterials: ['', Validators.required],
            storageFacilitiesExist: ['', Validators.required],
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

        // this.sta3FormA = new FormGroup(
        //     {
        //         produceOrdersOrStock: new FormControl(),
        //         issueWorkOrderOrEquivalent: new FormControl(),
        //         identifyBatchAsSeparate: new FormControl(),
        //         productsContainersCarryWorksOrder: new FormControl(),
        //         isolatedCaseDoubtfulQuality: new FormControl(),
        //
        //         headQaQualificationsTraining: new FormControl(),
        //         reportingTo: new FormControl(),
        //         separateQcid: new FormControl(),
        //         testsRelevantStandard: new FormControl(),
        //         spoComingMaterials: new FormControl(),
        //         spoProcessOperations: new FormControl(),
        //         spoFinalProducts: new FormControl(),
        //         monitoredQcs: new FormControl(),
        //         qauditChecksCarried: new FormControl(),
        //         informationQcso: new FormControl(),
        //
        //         mainMaterialsPurchasedSpecification: new FormControl(),
        //         adoptedReceiptMaterials: new FormControl(),
        //         storageFacilitiesExist: new FormControl(),
        //
        //         stepsManufacture: new FormControl(),
        //         maintenanceSystem: new FormControl(),
        //         qcsSupplement: new FormControl(),
        //         qmInstructions: new FormControl(),
        //         testEquipmentUsed: new FormControl(),
        //         indicateExternalArrangement: new FormControl(),
        //         levelDefectivesFound: new FormControl(),
        //         levelClaimsComplaints: new FormControl(),
        //         independentTests: new FormControl(),
        //         indicateStageManufacture: new FormControl(),
        //     }
        // );

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
    get formSta1Form(): any{
        return this.sta1Form.controls;
    }
    get formSta3FormA(): any{
        return this.sta3FormA.controls;
    }

    onClickSaveSTA1(valid: boolean) {
        if (valid) {
            if (this.permitEntityDetails == null) {
                this.qaService.savePermitSTA1('1', this.sta1Form.value).subscribe(
                    (data: PermitEntityDetails) => {
                        this.permitEntityDetails = data;
                        console.log(data);
                        // this.router.navigate(['/users-list']);
                    },
                );
            } else {
                this.qaService.updatePermitSTA1(String(this.permitEntityDetails.id), this.sta1Form.value).subscribe(
                    (data: PermitEntityDetails) => {
                        this.permitEntityDetails = data;
                        console.log(data);
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
                    // this.router.navigate(['/users-list']);
                },
            );
        }
    }

    onClickUpdateSTA3A(valid: boolean) {
        if (valid) {
            this.qaService.updatePermitSTA3(this.permitEntityDetails.id.toString(), this.sta3FormA.value).subscribe(
                (data: any) => {
                    console.log(data);
                    // this.router.navigate(['/users-list']);
                },
            );
        }
    }
}
