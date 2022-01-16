import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-add-edit-auction-item',
    templateUrl: './add-edit-auction-item.component.html',
    styleUrls: ['./add-edit-auction-item.component.css']
})
export class AddEditAuctionItemComponent implements OnInit {
    message: String
    form: FormGroup
    loading = false
    categories: any

    constructor(private fb: FormBuilder, private dialodRef: MatDialogRef<any>) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            serialNo: [null, Validators.required],
            itemName: [null, Validators.required],
            itemType: [null, Validators.required],
            quantity: [null, Validators.required],
            tareWeight: [null],
            chassisNo: [null],
            bodyType: [null],
            transmission: [null],
            model: [null],
            make: [null],
            fuel: [null],
            typeOfVehicle: [null]
        })
        // this.form.valueChanges
        //     .subscribe(
        //         res=>{
        //             console.log(res)
        //         }
        //     )
    }

    saveRecord(){
        this.dialodRef.close(this.form.value)
    }

}
