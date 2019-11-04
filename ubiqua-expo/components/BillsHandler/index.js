import React from "react";
import { ScrollView, View } from "react-native";
import Conta from "./Conta";

export default function BillsHandler(props) {
  const { bills, handleCheck, handleEditButton } = props;
  const mapBills = bills ? (
    bills.map((el, index) => {
      return (
        <View key={index}>
          <Conta
            text={el.name}
            bill={el.bill}
            handleEditButton={() => {
              handleEditButton(index);
            }}
            whenChecked={() => {
              handleCheck(index);
            }}
          />
        </View>
      );
    })
  ) : (
    <View></View>
  );

  return <ScrollView>{mapBills}</ScrollView>;
}
