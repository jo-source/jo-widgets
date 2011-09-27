/*
 * Copyright (c) 2011, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.common.types;

import java.util.HashMap;
import java.util.Map;

public enum VirtualKey {

	F1(null, "F1"), //$NON-NLS-1$
	F2(null, "F2"), //$NON-NLS-1$
	F3(null, "F3"), //$NON-NLS-1$
	F4(null, "F4"), //$NON-NLS-1$
	F5(null, "F5"), //$NON-NLS-1$
	F6(null, "F6"), //$NON-NLS-1$
	F7(null, "F7"), //$NON-NLS-1$
	F8(null, "F8"), //$NON-NLS-1$
	F9(null, "F9"), //$NON-NLS-1$
	F10(null, "F10"), //$NON-NLS-1$
	F11(null, "F11"), //$NON-NLS-1$
	F12(null, "F12"), //$NON-NLS-1$

	ENTER(null, Messages.getString("VirtualKey.enter")), //$NON-NLS-1$
	BACK_SPACE(null, Messages.getString("VirtualKey.back_space")), //$NON-NLS-1$
	TAB(null, Messages.getString("VirtualKey.tab")), //$NON-NLS-1$
	SHIFT(null, Messages.getString("VirtualKey.shift")), //$NON-NLS-1$
	CONTROL(null, Messages.getString("VirtualKey.ctrl")), //$NON-NLS-1$
	ALT(null, Messages.getString("VirtualKey.alt")), //$NON-NLS-1$
	PAUSE(null, Messages.getString("VirtualKey.pause")), //$NON-NLS-1$
	CAPS_LOCK(null, Messages.getString("VirtualKey.caps_lock")), //$NON-NLS-1$
	ESC(null, Messages.getString("VirtualKey.esc")), //$NON-NLS-1$
	SPACE(null, Messages.getString("VirtualKey.space")), //$NON-NLS-1$
	PAGE_UP(null, Messages.getString("VirtualKey.page_up")), //$NON-NLS-1$
	PAGE_DOWN(null, Messages.getString("VirtualKey.page_down")), //$NON-NLS-1$
	END(null, Messages.getString("VirtualKey.end")), //$NON-NLS-1$
	HOME(null, Messages.getString("VirtualKey.home")), //$NON-NLS-1$
	DELETE(null, Messages.getString("VirtualKey.delete")), //$NON-NLS-1$
	INSERT(null, Messages.getString("VirtualKey.insert")), //$NON-NLS-1$

	ARROW_LEFT(null, Messages.getString("VirtualKey.arrow_left")), //$NON-NLS-1$
	ARROW_UP(null, Messages.getString("VirtualKey.arrow_up")), //$NON-NLS-1$
	ARROW_RIGHT(null, Messages.getString("VirtualKey.arrow_right")), //$NON-NLS-1$
	ARROW_DOWN(null, Messages.getString("VirtualKey.arrow_down")), //$NON-NLS-1$

	DIGIT_0(Character.valueOf('0'), "0"), //$NON-NLS-1$
	DIGIT_1(Character.valueOf('1'), "1"), //$NON-NLS-1$
	DIGIT_2(Character.valueOf('2'), "2"), //$NON-NLS-1$
	DIGIT_3(Character.valueOf('3'), "3"), //$NON-NLS-1$
	DIGIT_4(Character.valueOf('4'), "4"), //$NON-NLS-1$
	DIGIT_5(Character.valueOf('5'), "5"), //$NON-NLS-1$
	DIGIT_6(Character.valueOf('6'), "6"), //$NON-NLS-1$
	DIGIT_7(Character.valueOf('7'), "7"), //$NON-NLS-1$
	DIGIT_8(Character.valueOf('8'), "8"), //$NON-NLS-1$
	DIGIT_9(Character.valueOf('9'), "9"), //$NON-NLS-1$

	A(Character.valueOf('A'), "A"), //$NON-NLS-1$
	B(Character.valueOf('B'), "B"), //$NON-NLS-1$
	C(Character.valueOf('C'), "C"), //$NON-NLS-1$
	D(Character.valueOf('D'), "D"), //$NON-NLS-1$
	E(Character.valueOf('E'), "E"), //$NON-NLS-1$
	F(Character.valueOf('F'), "F"), //$NON-NLS-1$
	G(Character.valueOf('G'), "G"), //$NON-NLS-1$
	H(Character.valueOf('H'), "H"), //$NON-NLS-1$
	I(Character.valueOf('I'), "I"), //$NON-NLS-1$
	J(Character.valueOf('J'), "J"), //$NON-NLS-1$
	K(Character.valueOf('K'), "K"), //$NON-NLS-1$
	L(Character.valueOf('L'), "L"), //$NON-NLS-1$
	M(Character.valueOf('M'), "M"), //$NON-NLS-1$
	N(Character.valueOf('N'), "N"), //$NON-NLS-1$
	O(Character.valueOf('O'), "O"), //$NON-NLS-1$
	P(Character.valueOf('P'), "P"), //$NON-NLS-1$
	Q(Character.valueOf('Q'), "Q"), //$NON-NLS-1$
	R(Character.valueOf('R'), "R"), //$NON-NLS-1$
	S(Character.valueOf('S'), "S"), //$NON-NLS-1$
	T(Character.valueOf('T'), "T"), //$NON-NLS-1$
	U(Character.valueOf('U'), "U"), //$NON-NLS-1$
	V(Character.valueOf('V'), "V"), //$NON-NLS-1$
	W(Character.valueOf('W'), "W"), //$NON-NLS-1$
	X(Character.valueOf('X'), "X"), //$NON-NLS-1$
	Y(Character.valueOf('Y'), "Y"), //$NON-NLS-1$
	Z(Character.valueOf('Z'), "Z"), //$NON-NLS-1$

	/**
	 * The key pressed or released is not defined as a virtual key.
	 * This must not be used to define accelerators.
	 */
	UNDEFINED(null, Messages.getString("VirtualKey.undefined")); //$NON-NLS-1$

	private static Map<Character, VirtualKey> characterToVirtualKey;

	private final Character character;
	private final String label;

	private VirtualKey(final Character character, final String label) {
		this.character = character;
		this.label = label;
	}

	/**
	 * Gets the label of the key.
	 * 
	 * @return The label of the key
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Gets a character representation of the virtual key, or null, if the virtual key is not
	 * representable by a character.
	 * 
	 * @return The character representation or null
	 */
	public Character getCharacter() {
		return character;
	}

	/**
	 * @return true, if the virtual key is representable by a character, false otherwise.
	 */
	public boolean isCharacter() {
		return character != null;
	}

	/**
	 * Gets the virtual key for a char.
	 * 
	 * @param character The char to get the virtual key for
	 * @return The virtual key for the char or null, if no virtual key exists for the char
	 */
	public static VirtualKey toVirtualKey(final char character) {
		return getCharacterToVirtualKey().get(Character.valueOf(character));
	}

	private static Map<Character, VirtualKey> getCharacterToVirtualKey() {
		if (characterToVirtualKey == null) {
			characterToVirtualKey = new HashMap<Character, VirtualKey>();
			characterToVirtualKey.put(Character.valueOf('0'), VirtualKey.DIGIT_0);
			characterToVirtualKey.put(Character.valueOf('1'), VirtualKey.DIGIT_1);
			characterToVirtualKey.put(Character.valueOf('2'), VirtualKey.DIGIT_2);
			characterToVirtualKey.put(Character.valueOf('3'), VirtualKey.DIGIT_3);
			characterToVirtualKey.put(Character.valueOf('4'), VirtualKey.DIGIT_4);
			characterToVirtualKey.put(Character.valueOf('5'), VirtualKey.DIGIT_5);
			characterToVirtualKey.put(Character.valueOf('6'), VirtualKey.DIGIT_6);
			characterToVirtualKey.put(Character.valueOf('7'), VirtualKey.DIGIT_7);
			characterToVirtualKey.put(Character.valueOf('8'), VirtualKey.DIGIT_8);
			characterToVirtualKey.put(Character.valueOf('9'), VirtualKey.DIGIT_9);

			characterToVirtualKey.put(Character.valueOf('a'), VirtualKey.A);
			characterToVirtualKey.put(Character.valueOf('b'), VirtualKey.B);
			characterToVirtualKey.put(Character.valueOf('c'), VirtualKey.C);
			characterToVirtualKey.put(Character.valueOf('d'), VirtualKey.D);
			characterToVirtualKey.put(Character.valueOf('e'), VirtualKey.E);
			characterToVirtualKey.put(Character.valueOf('f'), VirtualKey.F);
			characterToVirtualKey.put(Character.valueOf('g'), VirtualKey.G);
			characterToVirtualKey.put(Character.valueOf('h'), VirtualKey.H);
			characterToVirtualKey.put(Character.valueOf('i'), VirtualKey.I);
			characterToVirtualKey.put(Character.valueOf('j'), VirtualKey.J);
			characterToVirtualKey.put(Character.valueOf('k'), VirtualKey.K);
			characterToVirtualKey.put(Character.valueOf('l'), VirtualKey.L);
			characterToVirtualKey.put(Character.valueOf('m'), VirtualKey.M);
			characterToVirtualKey.put(Character.valueOf('n'), VirtualKey.N);
			characterToVirtualKey.put(Character.valueOf('o'), VirtualKey.O);
			characterToVirtualKey.put(Character.valueOf('p'), VirtualKey.P);
			characterToVirtualKey.put(Character.valueOf('q'), VirtualKey.Q);
			characterToVirtualKey.put(Character.valueOf('r'), VirtualKey.R);
			characterToVirtualKey.put(Character.valueOf('s'), VirtualKey.S);
			characterToVirtualKey.put(Character.valueOf('t'), VirtualKey.T);
			characterToVirtualKey.put(Character.valueOf('u'), VirtualKey.U);
			characterToVirtualKey.put(Character.valueOf('v'), VirtualKey.V);
			characterToVirtualKey.put(Character.valueOf('w'), VirtualKey.W);
			characterToVirtualKey.put(Character.valueOf('x'), VirtualKey.X);
			characterToVirtualKey.put(Character.valueOf('y'), VirtualKey.Y);
			characterToVirtualKey.put(Character.valueOf('z'), VirtualKey.Z);

			characterToVirtualKey.put(Character.valueOf('A'), VirtualKey.A);
			characterToVirtualKey.put(Character.valueOf('B'), VirtualKey.B);
			characterToVirtualKey.put(Character.valueOf('C'), VirtualKey.C);
			characterToVirtualKey.put(Character.valueOf('D'), VirtualKey.D);
			characterToVirtualKey.put(Character.valueOf('E'), VirtualKey.E);
			characterToVirtualKey.put(Character.valueOf('F'), VirtualKey.F);
			characterToVirtualKey.put(Character.valueOf('G'), VirtualKey.G);
			characterToVirtualKey.put(Character.valueOf('G'), VirtualKey.H);
			characterToVirtualKey.put(Character.valueOf('I'), VirtualKey.I);
			characterToVirtualKey.put(Character.valueOf('J'), VirtualKey.J);
			characterToVirtualKey.put(Character.valueOf('K'), VirtualKey.K);
			characterToVirtualKey.put(Character.valueOf('L'), VirtualKey.L);
			characterToVirtualKey.put(Character.valueOf('M'), VirtualKey.M);
			characterToVirtualKey.put(Character.valueOf('N'), VirtualKey.N);
			characterToVirtualKey.put(Character.valueOf('O'), VirtualKey.O);
			characterToVirtualKey.put(Character.valueOf('P'), VirtualKey.P);
			characterToVirtualKey.put(Character.valueOf('Q'), VirtualKey.Q);
			characterToVirtualKey.put(Character.valueOf('R'), VirtualKey.R);
			characterToVirtualKey.put(Character.valueOf('S'), VirtualKey.S);
			characterToVirtualKey.put(Character.valueOf('T'), VirtualKey.T);
			characterToVirtualKey.put(Character.valueOf('U'), VirtualKey.U);
			characterToVirtualKey.put(Character.valueOf('V'), VirtualKey.V);
			characterToVirtualKey.put(Character.valueOf('W'), VirtualKey.W);
			characterToVirtualKey.put(Character.valueOf('X'), VirtualKey.X);
			characterToVirtualKey.put(Character.valueOf('Y'), VirtualKey.Y);
			characterToVirtualKey.put(Character.valueOf('Z'), VirtualKey.Z);
		}
		return characterToVirtualKey;
	}

}
