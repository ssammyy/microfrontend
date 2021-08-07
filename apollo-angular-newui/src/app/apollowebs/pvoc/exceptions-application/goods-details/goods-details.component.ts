import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {WaiverProductComponent} from "../../waivers/waiver-product/waiver-product.component";
import {RawMaterialsComponent} from "../../waivers/raw-materials/raw-materials.component";
import {MainProductionMachineryComponent} from "../../waivers/main-production-machinery/main-production-machinery.component";
import {IndustrialSparesComponent} from "../../waivers/industrial-spares/industrial-spares.component";

@Component({
    selector: 'app-goods-details',
    templateUrl: './goods-details.component.html',
    styleUrls: ['./goods-details.component.css']
})
export class GoodsDetailsComponent implements OnInit {

    constructor(private dialog: MatDialog) {
    }

    ngOnInit(): void {
    }

    addGoodsManufactured() {
        let dialogRef = this.dialog.open(WaiverProductComponent, {});
    }

    addRawMaterials() {
        let dialogRef = this.dialog.open(RawMaterialsComponent, {})
    }

    addMachinery() {
        let dialogRef = this.dialog.open(MainProductionMachineryComponent, {})
    }
    addSpareParts() {
        let dialogRef = this.dialog.open(IndustrialSparesComponent, {})
    }

}
