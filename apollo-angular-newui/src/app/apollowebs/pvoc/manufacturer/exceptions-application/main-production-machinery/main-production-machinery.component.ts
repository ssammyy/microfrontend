import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-main-production-machinery',
    templateUrl: './main-production-machinery.component.html',
    styleUrls: ['./main-production-machinery.component.css']
})
export class MainProductionMachineryComponent implements OnInit {
    productMachinery: FormGroup

    constructor(private fb: FormBuilder, private dialogRef: MatDialogRef<any>) {
    }

    ngOnInit(): void {
        this.productMachinery = this.fb.group({
            hsCode: ['', Validators.required],
            machineDescription: ['', Validators.required],
            makeModel: ['', Validators.required],
            countryOfOrigin: ['', Validators.required]
        })
    }

    addMachinery() {
        this.dialogRef.close(this.productMachinery.value)
    }

}
