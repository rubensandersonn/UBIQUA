import React, { useState } from "react";

import { Wrapper, Texto, EditButton, Line } from "./style";
import { TextWhite, RoundButton } from "../../utils/styled";

export default function Sensor(props) {
  const { title, environment, handleButton, textButton, styleButton } = props;

  return (
    <Wrapper>
      <Line horizontal={true}>
        <Texto>{title ? title : ""} - </Texto>
        <Texto> {environment ? environment : 0}</Texto>
      </Line>
      <RoundButton style={styleButton} onPress={handleButton}>
        <TextWhite style={{ color: "black", fontWeight: "600" }}>
          {textButton}
        </TextWhite>
      </RoundButton>
    </Wrapper>
  );
}
