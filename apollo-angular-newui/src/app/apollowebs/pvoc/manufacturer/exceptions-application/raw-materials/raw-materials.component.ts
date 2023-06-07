import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-raw-materials',
    templateUrl: './raw-materials.component.html',
    styleUrls: ['./raw-materials.component.css']
})
export class RawMaterialsComponent implements OnInit {
    rawMaterial: FormGroup

    constructor(private fb: FormBuilder, private dialogRef: MatDialogRef<any>) {
    }

    ngOnInit(): void {
        this.rawMaterial = this.fb.group({
            hsCode: ['', Validators.required],
            rawMaterialDescription: ['', Validators.required],
            endProduct: ['', Validators.required],
            countryOfOrigin: ['', Validators.required]
        })
    }

    addRawMaterial() {
        this.dialogRef.close(this.rawMaterial.value)
    }

}
