icyFile = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            policyFile = new FileReader(MAC_PERMISSIONS);
            Slog.d(TAG, "Using policy file " + MAC_PERMISSIONS);

            parser.setInput(policyFile);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "policy");

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                switch (parser.getName()) {
                    case "signer":
                        policies.add(readSignerOrThrow(parser));
                        break;
                    default:
                        skip(parser);
                }
            }
        } catch (IllegalStateException | IllegalArgumentException |
                XmlPullParserException ex) {
            StringBuilder sb = new StringBuilder("Exception @");
            sb.append(parser.getPositionDescription());
            sb.append(" while parsing ");
            sb.append(MAC_PERMISSIONS);
            sb.append(":");
            sb.append(ex);
            Slog.w(TAG, sb.toString());
            return false;
        } catch (IOException ioe) {
            Slog.w(TAG, "Exception parsing " + MAC_PERMISSIONS, ioe);
            return false;
        } finally {
            IoUtils.closeQuietly(policyFile);
        }

        // Now sort the policy stanzas
        PolicyComparator policySort = new PolicyComparator();
        Collections.sort(policies, policySort);
        if (policySort.foundDuplicate()) {
            Slog.w(TAG, "ERROR! Duplicate entries found parsing " + MAC_PERMISSIONS);
            return false;
        }

        synchronized (sPolicies) {
            sPolicies = policies;

            if (DEBUG_POLICY_ORDER) {
                for (Policy policy : sPolicies) {
                    Slog.d(TAG, "Policy: " + policy.toString());
                }
            }
        }

        return true;
    }

    /**
     * Loop over a signer tag looking for seinfo, package and cert tags. A {@link Policy}
     * instance will be created and returned in the process. During the pass all other
     * tag elements will be skipped.
     *
     * @param parser an XmlPullParser object representing a signer element.
     * @return the constructed {@link Policy} instance
     * @throws IOException
     * @throws XmlPullParserException
     * @throws IllegalArgumentException if any of the validation checks fail while
     *         parsing tag values.
     * @throws IllegalStateException if any of the invariants fail when constructing
     *         the {@link Policy} instance.
     */
    private static Policy readSignerOrThrow(XmlPullParser parser) throws IOException,
            XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, null, "signer");
        Policy.PolicyBuilder pb = new Policy.PolicyBuilder();

        // Check for a cert attached to the signer tag. We allow a signature
        // to appear as an attribute as well as those attached to cert tags.
        String cert = parser.getAttributeValue(null, "signature");
        if (cert != null) {
            pb.addSignature(cert);
        }

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.S