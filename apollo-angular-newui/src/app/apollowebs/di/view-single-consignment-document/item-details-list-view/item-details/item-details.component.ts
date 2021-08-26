import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";
import swal from "sweetalert2";

@Component({
  selector: 'app-item-details',
  templateUrl: './item-details.component.html',
  styleUrls: ['./item-details.component.css']
})
export class ItemDetailsComponent implements OnInit {
  itemId: any
  cdUuid: any
  itemDetails: any
  constructor(private activatedRoute: ActivatedRoute,private diService: DestinationInspectionService,
              private router: Router) { }

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe(
        res=>{
          this.itemId=res.get("id")
          this.cdUuid=res.get("cdUuid")
          this.loadItemDetails()
        }
    )
  }

  loadItemDetails() {
    this.diService.loadItemDetails(this.itemId)
        .subscribe(
            res => {
              if (res.responseCode === "00") {
                this.itemDetails=res.data
              } else {
                swal.fire({
                  title: res.message,
                  buttonsStyling: false,
                  customClass: {
                    confirmButton: 'btn btn-success form-wizard-next-btn ',
                  },
                  icon: 'error'
                }).then(()=>{
                  this.router.navigate(["/di/",this.cdUuid])
                });
              }
            }
        )
  }

}
