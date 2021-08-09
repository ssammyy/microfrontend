import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {WaiverProductComponent} from "../../waivers/waiver-product/waiver-product.component";
import {RawMaterialsComponent} from "../../waivers/raw-materials/raw-materials.component";
import {MainProductionMachineryComponent} from "../../waivers/main-production-machinery/main-production-machinery.component";
import {IndustrialSparesComponent} from "../../waivers/industrial-spares/industrial-spares.component";
import {FormBuilder, FormGroup} from "@angular/forms";
import {StepperDataService} from "../../../../core/services/data/stepper-data.service";

@Component({
    selector: 'app-goods-details',
    templateUrl: './goods-details.component.html',
    styleUrls: ['./goods-details.component.css']
})
export class GoodsDetailsComponent implements OnInit {
    private labReports: any[];
    private qmsReports: any[];
    private productList: any[];
    @Input() goodDetails: any;

    constructor(private dialog: MatDialog, private data: StepperDataService) {
    }

    ngOnInit(): void {
        this.goodDetails = {
            products: [],
            rawMaterials: [],
            spares: [],
            mainMachinary: []
        }
        this.data.dataChange.asObservable()
            .subscribe(
                res => {
                    this.goodDetails = res
                }
            )

    }

    addGoodsManufactured() {
        let dialogRef = this.dialog.open(WaiverProductComponent, {});
        dialogRef.afterClosed()
            .subscribe(res => {
                if (res) {
                    if (!this.goodDetails.products) {
                        this.goodDetails['products'] = []
                    }
                    this.goodDetails.products.push(res)
                    this.data.setData(this.goodDetails)
                }
            })
    }

    addRawMaterials() {
        let dialogRef = this.dialog.open(RawMaterialsComponent, {})
        dialogRef.afterClosed()
            .subscribe(res => {
                if (res) {
                    if (!this.goodDetails.rawMaterials) {
                        this.goodDetails['rawMaterials'] = []
                    }
                    this.goodDetails.rawMaterials.push(res)
                    this.data.setData(this.goodDetails)
                }
            })
    }

    addMachinery() {
        let dialogRef = this.dialog.open(MainProductionMachineryComponent, {})
        dialogRef.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        if (!this.goodDetails.mainMachinary) {
                            this.goodDetails['mainMachinary'] = []
                        }
                        this.goodDetails.mainMachinary.push(res)
                        this.data.setData(this.goodDetails)
                    }
                }
            )
    }

    addSpareParts() {
        let dialogRef = this.dialog.open(IndustrialSparesComponent, {})
        dialogRef.afterClosed()
            .subscribe(res => {
                if (res) {
                    if (!this.goodDetails.spares) {
                        this.goodDetails['spares'] = []
                    }
                    this.goodDetails.spares.push(res)
                    this.data.setData(this.goodDetails)
                }
            })
    }

}
